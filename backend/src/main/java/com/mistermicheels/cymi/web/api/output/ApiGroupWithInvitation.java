package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;
import com.mistermicheels.cymi.component.group.entity.IGroupInvitationWithGroup;

public class ApiGroupWithInvitation extends ApiGroup {

    private final GroupMembershipRole userRole;

    public ApiGroupWithInvitation(IGroupInvitationWithGroup invitation) {
        super(invitation.getGroup());
        this.userRole = invitation.getRole();
    }

    public GroupMembershipRole getUserRole() {
        return this.userRole;
    }

}
