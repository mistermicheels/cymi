package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.event.Event;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiEventWithGroup extends ApiEvent {

    private final Long groupId;
    private final String groupName;

    private GroupMembershipRole userRoleInGroup;

    public ApiEventWithGroup(Event event) {
        super(event);
        this.groupId = event.getGroupId();
        this.groupName = event.getGroupName();
    }

    public void setUserRoleInGroup(GroupMembershipRole userRoleInGroup) {
        this.userRoleInGroup = userRoleInGroup;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public GroupMembershipRole getUserRoleInGroup() {
        return this.userRoleInGroup;
    }

}
