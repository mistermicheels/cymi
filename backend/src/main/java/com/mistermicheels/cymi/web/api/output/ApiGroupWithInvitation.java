package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupInvitation;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class ApiGroupWithInvitation extends ApiGroup {

    private final GroupMembershipRole userRole;

    public ApiGroupWithInvitation(GroupInvitation invitation) {
        super(invitation.getGroup());
        this.userRole = invitation.getRole();
    }

    public GroupMembershipRole getUserRole() {
        return this.userRole;
    }

}
