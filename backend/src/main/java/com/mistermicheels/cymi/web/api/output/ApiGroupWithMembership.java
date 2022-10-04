package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;
import com.mistermicheels.cymi.component.group.entity.IGroupMembershipWithGroup;

public class ApiGroupWithMembership extends ApiGroup {

    private final GroupMembershipRole userRole;
    private final String userDisplayName;

    public ApiGroupWithMembership(IGroupMembershipWithGroup membership) {
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
