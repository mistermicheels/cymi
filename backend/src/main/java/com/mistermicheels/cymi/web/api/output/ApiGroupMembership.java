package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiGroupMembership {

    private final Long groupId;
    private final GroupMembershipRole role;
    private final String displayName;

    public ApiGroupMembership(GroupMembership membership) {
        this.groupId = membership.getGroupId();
        this.role = membership.getRole();
        this.displayName = membership.getDisplayName();
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public GroupMembershipRole getRole() {
        return this.role;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
