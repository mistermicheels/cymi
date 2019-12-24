package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiGroupWithMembership extends ApiGroup {

    private final GroupMembershipRole userRole;
    private final String userDisplayName;

    public ApiGroupWithMembership(GroupMembership membership) {
        super(membership.getGroup());
        this.userRole = membership.getRole();
        this.userDisplayName = membership.getDisplayName();
    }

    public GroupMembershipRole getUserRole() {
        return this.userRole;
    }

    public String getUserDisplayName() {
        return this.userDisplayName;
    }

}
