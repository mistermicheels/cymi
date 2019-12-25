package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiGroupMembership {

    private final Long groupId;
    private final GroupMembershipRole role;
    private final String displayName;
    private final boolean isCurrentUser;

    public ApiGroupMembership(GroupMembership membership, Long currentUserId) {
        this.groupId = membership.getGroupId();
        this.role = membership.getRole();
        this.displayName = membership.getDisplayName();
        this.isCurrentUser = (membership.getUserId() == currentUserId);
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

    public boolean getIsCurrentUser() {
        return this.isCurrentUser;
    }

}
