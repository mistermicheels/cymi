package com.mistermicheels.cymi.web.api.controller.events;

import java.util.List;
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
import com.mistermicheels.cymi.component.event.EventService;
import com.mistermicheels.cymi.config.security.CustomUserDetails;
import com.mistermicheels.cymi.web.api.controller.events.input.CreateEventInput;
import com.mistermicheels.cymi.web.api.controller.events.input.RespondInput;
import com.mistermicheels.cymi.web.api.output.ApiEvent;
import com.mistermicheels.cymi.web.api.output.ApiEventWithGroup;
import com.mistermicheels.cymi.web.api.output.ApiSuccessResponse;

@RestController()
@RequestMapping("events")
public class EventsController {

    private final EventService eventService;

    @Autowired
    public EventsController(EventService eventService) {
        this.eventService = eventService;
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
    public ApiEvent getByIdOrThrow(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Event event = this.eventService.findWithGroupByIdOrThrow(id, userDetails.getId());
        return new ApiEventWithGroup(event, userDetails.getId());
    }

    @GetMapping("/upcoming/user")
    public List<ApiEventWithGroup> getUpcomingForUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.eventService.findUpcomingWithGroupWithOwnResponseForUser(userDetails.getId())
                .stream().map(event -> new ApiEventWithGroup(event, userDetails.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/upcoming/group/{groupId}")
    public List<ApiEvent> getUpcomingByGroup(@PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return this.eventService.findUpcomingWithOwnResponseByGroup(groupId, userDetails.getId())
                .stream().map(event -> new ApiEvent(event, userDetails.getId()))
                .collect(Collectors.toList());
    }

}
