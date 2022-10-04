package com.mistermicheels.cymi.web.api.output;

import org.springframework.lang.Nullable;

import com.mistermicheels.cymi.component.event.entity.Event;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiEventWithGroup extends ApiEvent {

    private final Long groupId;
    private final String groupName;

    @Nullable
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

    public @Nullable GroupMembershipRole getUserRoleInGroup() {
        return this.userRoleInGroup;
    }

}
