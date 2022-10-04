package com.mistermicheels.cymi.component.group.entity;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public interface IGroupInvitation {

    Long getGroupId();

    Long getUserId();

    GroupMembershipRole getRole();

}
