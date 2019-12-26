package com.mistermicheels.cymi.component.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.group.Group;
import com.mistermicheels.cymi.component.group.GroupService;

@Service
public class EventService {

    private final GroupService groupService;
    private final EventRepository repository;

    @Autowired
    public EventService(GroupService groupService, EventRepository repository) {
        this.groupService = groupService;
        this.repository = repository;
    }

    public Event createEvent(Long groupId, EventBasicData basicData, Long currentUserId) {
        this.groupService.checkCurrentUserAdmin(groupId, currentUserId);
        Group group = this.groupService.getReference(groupId);
        Event event = new Event(group, basicData);
        this.repository.save(event);
        return event;
    }

    public Event findWithGroupByIdOrThrow(Long eventId, Long currentUserId) {
        Event event = this.repository.findWithGroupById(eventId)
                .orElseThrow(() -> new InvalidRequestException("Invalid event ID"));

        this.groupService.checkCurrentUserMember(event.getGroupId(), currentUserId);
        return event;
    }

    public List<Event> findUpcomingByGroup(Long groupId, Long currentUserId) {
        this.groupService.checkCurrentUserMember(groupId, currentUserId);
        return this.repository.findUpcomingByGroupId(groupId);
    }

    public List<Event> findUpcomingWithGroupForUser(Long userId) {
        return this.repository.findUpcomingWithGroupByUserId(userId);
    }

}
