package com.mistermicheels.cymi.component.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.group.Group;
import com.mistermicheels.cymi.component.group.GroupService;
import com.mistermicheels.cymi.component.user.User;
import com.mistermicheels.cymi.component.user.UserService;

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

    public Event createEvent(Long groupId, EventBasicData basicData, Long currentUserId) {
        this.groupService.checkCurrentUserAdmin(groupId, currentUserId);
        Group group = this.groupService.getReference(groupId);
        Event event = new Event(group, basicData);
        this.repository.save(event);
        return event;
    }

    public void respond(Long eventId, Long currentUserId, EventResponseStatus status) {
        this.respond(eventId, currentUserId, status, null);
    }

    public void respond(Long eventId, Long currentUserId, EventResponseStatus status,
            String comment) {
        Event event = this.repository.findById(eventId)
                .orElseThrow(() -> new InvalidRequestException("There is no event with that ID"));

        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);

        User user = this.userService.getReference(currentUserId);
        EventResponse response;

        if (comment != null) {
            response = new EventResponse(event, user, status, comment);
        } else {
            response = new EventResponse(event, user, status);
        }

        this.eventResponseRepository.save(response);
    }

    public Event findWithGroupByIdOrThrow(Long eventId, Long currentUserId) {
        Event event = this.repository.findWithGroupById(eventId)
                .orElseThrow(() -> new InvalidRequestException("Invalid event ID"));

        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);
        return event;
    }

    public List<Event> findUpcomingWithOwnResponseByGroup(Long groupId, Long currentUserId) {
        this.groupService.checkCurrentUserMember(groupId, currentUserId);
        return this.repository.findUpcomingWithResponsesByGroupId(groupId);
    }

    public List<Event> findUpcomingWithGroupWithOwnResponseForUser(Long userId) {
        return this.repository.findUpcomingWithGroupWithResponsesByUserId(userId);
    }

}
