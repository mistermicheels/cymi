package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;
import com.mistermicheels.cymi.component.group.entity.GroupInvitation;

public class ApiGroupInvitation {

    private final Long groupId;
    private final GroupMembershipRole role;
    private final String email;

    public ApiGroupInvitation(GroupInvitation invitation) {
        this.groupId = invitation.getGroupId();
        this.role = invitation.getRole();
        this.email = invitation.getUserEmail();
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public String getEmail() {
        return this.email;
    }

    public GroupMembershipRole getRole() {
        return this.role;
    }

}
