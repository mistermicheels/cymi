package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.event.Event;

public class ApiEventWithGroup extends ApiEvent {

    private final Long groupId;
    private final String groupName;

    public ApiEventWithGroup(Event event, Long currentUserId) {
        super(event, currentUserId);
        this.groupId = event.getGroupId();
        this.groupName = event.getGroupName();
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

}
