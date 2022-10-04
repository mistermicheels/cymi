package com.mistermicheels.cymi.component.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.event.entity.Event;
import com.mistermicheels.cymi.component.event.entity.EventResponse;
import com.mistermicheels.cymi.component.group.GroupService;
import com.mistermicheels.cymi.component.group.entity.Group;
import com.mistermicheels.cymi.component.user.UserService;
import com.mistermicheels.cymi.component.user.entity.User;

@Service
public class EventService {

    private final UserService userService;
    private final GroupService groupService;
    private final EventRepository repository;
    private final EventResponseRepository eventResponseRepository;

    @Autowired
    public EventService(UserService userService, GroupService groupService,
            EventRepository repository, EventResponseRepository eventResponseRepository) {
        this.userService = userService;
        this.groupService = groupService;
        this.repository = repository;
        this.eventResponseRepository = eventResponseRepository;
    }

    public Event createEvent(Long groupId, EventCreationDto eventData, Long currentUserId) {
        this.groupService.checkCurrentUserAdmin(groupId, currentUserId);
        Group group = this.groupService.getReference(groupId);
        Event event = new Event(group, eventData);
        this.repository.save(event);
        return event;
    }

    public void respond(Long eventId, Long currentUserId, EventResponseStatus status) {
        this.respond(eventId, currentUserId, status, null);
    }

    @Transactional
    public void respond(Long eventId, Long currentUserId, EventResponseStatus status,
            @Nullable String comment) {
        Event event = this.repository.findById(eventId).orElseThrow(this::getInvalidIdException);
        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);
        event.addResponseToResponseCounts(status);

        User user = this.userService.getReference(currentUserId);
        EventResponse response = new EventResponse(event, user, status, comment);

        this.repository.save(event);
        this.eventResponseRepository.save(response);
    }

    private InvalidRequestException getInvalidIdException() {
        return new InvalidRequestException("Invalid event ID");
    }

    public Event findByIdOrThrow(Long id, Long currentUserId) {
        Event event = this.repository.findById(id).orElseThrow(this::getInvalidIdException);
        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);
        return event;
    }

    public Event findWithGroupByIdOrThrow(Long id, Long currentUserId) {
        Event event = this.repository.findWithGroupById(id)
                .orElseThrow(this::getInvalidIdException);

        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);
        return event;
    }

    public List<Event> findUpcomingForGroup(Long groupId, Long currentUserId) {
        this.groupService.checkCurrentUserMember(groupId, currentUserId);
        return this.repository.findUpcomingByGroupId(groupId);
    }

    public List<Event> findUpcomingWithGroupForUser(Long userId) {
        return this.repository.findUpcomingWithGroupByUserId(userId);
    }

    public List<EventResponse> findOwnResponsesForEvents(List<Long> eventIds, Long currentUserId) {
        return this.eventResponseRepository
                .findByEventResponseIdUserIdAndEventResponseIdEventIdIn(currentUserId, eventIds);
    }

    public List<EventResponse> findResponsesForEvent(Long eventId, Long currentUserId) {
        Event event = this.repository.findById(eventId)
                .orElseThrow(() -> new InvalidRequestException("Invalid event ID"));

        this.groupService.checkCurrentUserAdmin(event.getGroupId(), currentUserId);
        return this.eventResponseRepository.findByEventResponseIdEventId(eventId);
    }

}
