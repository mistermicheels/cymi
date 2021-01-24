package com.mistermicheels.cymi.web.api.controller.events;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mistermicheels.cymi.component.event.Event;
import com.mistermicheels.cymi.component.event.EventBasicData;
import com.mistermicheels.cymi.component.event.EventResponse;
import com.mistermicheels.cymi.component.event.EventService;
import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;
import com.mistermicheels.cymi.component.group.GroupService;
import com.mistermicheels.cymi.config.security.CustomUserDetails;
import com.mistermicheels.cymi.web.api.controller.events.input.CreateEventInput;
import com.mistermicheels.cymi.web.api.controller.events.input.RespondInput;
import com.mistermicheels.cymi.web.api.output.ApiEvent;
import com.mistermicheels.cymi.web.api.output.ApiEventResponse;
import com.mistermicheels.cymi.web.api.output.ApiEventWithGroup;
import com.mistermicheels.cymi.web.api.output.ApiSuccessResponse;

@RestController()
@RequestMapping("events")
public class EventsController {

    private final EventService eventService;
    private final GroupService groupService;

    @Autowired
    public EventsController(EventService eventService, GroupService groupService) {
        this.eventService = eventService;
        this.groupService = groupService;
    }

    @PostMapping()
    public ApiSuccessResponse create(@Valid @RequestBody CreateEventInput input,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        EventBasicData basicData;

        if (input.description != null) {
            basicData = new EventBasicData(input.name, input.startTimestamp, input.endTimestamp,
                    input.location, input.description);
        } else {
            basicData = new EventBasicData(input.name, input.startTimestamp, input.endTimestamp,
                    input.location);
        }

        Event createdEvent = this.eventService.createEvent(input.groupId, basicData,
                userDetails.getId());

        return new ApiSuccessResponse(createdEvent.getId());
    }

    @PostMapping("/respond")
    public ApiSuccessResponse respond(@Valid @RequestBody RespondInput input,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails.getId();

        if (input.comment != null) {
            this.eventService.respond(input.eventId, currentUserId, input.status, input.comment);
        } else {
            this.eventService.respond(input.eventId, currentUserId, input.status);
        }

        return new ApiSuccessResponse();
    }

    @GetMapping("/id/{id}")
    public ApiEventWithGroup getByIdOrThrow(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Event event = this.eventService.findWithGroupByIdOrThrow(id, userDetails.getId());
        ApiEventWithGroup apiEvent = new ApiEventWithGroup(event);
        this.setOwnResponseOnApiEvents(List.of(apiEvent), userDetails.getId());
        this.setUserRoleInGroupOnApiEventsWithGroup(List.of(apiEvent), userDetails.getId());
        return apiEvent;
    }

    @GetMapping("/id/{id}/other_responses")
    public List<ApiEventResponse> getOtherResponsesForEventOrThrow(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Event event = this.eventService.findByIdOrThrow(id, userDetails.getId());

        List<GroupMembership> memberships = this.groupService
                .findMembershipsForGroup(event.getGroupId(), userDetails.getId());

        Map<Long, EventResponse> responsesByUserId = this.eventService
                .findResponsesForEvent(id, userDetails.getId()).stream()
                .collect(Collectors.toMap(EventResponse::getUserId, Function.identity()));

        return memberships.stream()
                .filter(membership -> membership.getUserId() != userDetails.getId())
                .filter(membership -> responsesByUserId.containsKey(membership.getUserId()))
                .map(membership -> new ApiEventResponse(
                        responsesByUserId.get(membership.getUserId()), membership))
                .collect(Collectors.toList());
    }

    @GetMapping("/upcoming/user")
    public List<ApiEventWithGroup> getUpcomingForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ApiEventWithGroup> apiEvents = this.eventService
                .findUpcomingWithGroupForUser(userDetails.getId()).stream()
                .map(event -> new ApiEventWithGroup(event)).collect(Collectors.toList());

        this.setOwnResponseOnApiEvents(apiEvents, userDetails.getId());
        this.setUserRoleInGroupOnApiEventsWithGroup(apiEvents, userDetails.getId());
        return apiEvents;
    }

    @GetMapping("/upcoming/group/{groupId}")
    public List<ApiEvent> getUpcomingByGroup(@PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ApiEvent> apiEvents = this.eventService
                .findUpcomingForGroup(groupId, userDetails.getId()).stream()
                .map(event -> new ApiEvent(event)).collect(Collectors.toList());

        this.setOwnResponseOnApiEvents(apiEvents, userDetails.getId());
        return apiEvents;
    }

    private void setOwnResponseOnApiEvents(List<? extends ApiEvent> apiEvents, Long currentUserId) {
        List<Long> eventIds = apiEvents.stream().map(ApiEvent::getId).collect(Collectors.toList());

        Map<Long, EventResponse> ownResponsesByEventId = this.eventService
                .findOwnResponsesForEvents(eventIds, currentUserId).stream()
                .collect(Collectors.toMap(EventResponse::getEventId, Function.identity()));

        for (ApiEvent apiEvent : apiEvents) {
            Long eventId = apiEvent.getId();

            if (ownResponsesByEventId.containsKey(eventId)) {
                apiEvent.setOwnResponse(ownResponsesByEventId.get(eventId));
            }
        }
    }

    private void setUserRoleInGroupOnApiEventsWithGroup(List<ApiEventWithGroup> apiEvents,
            Long currentUserId) {
        List<Long> groupIds = apiEvents.stream().map(ApiEventWithGroup::getGroupId).distinct()
                .collect(Collectors.toList());

        Map<Long, GroupMembershipRole> userRolesByGroup = this.groupService
                .findMembershipsOfUserInGroups(groupIds, currentUserId).stream()
                .collect(Collectors.toMap(GroupMembership::getGroupId, GroupMembership::getRole));

        for (ApiEventWithGroup apiEvent : apiEvents) {
            Long groupId = apiEvent.getGroupId();

            if (userRolesByGroup.containsKey(groupId)) {
                apiEvent.setUserRoleInGroup(userRolesByGroup.get(groupId));
            }
        }
    }

}
