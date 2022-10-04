package com.mistermicheels.cymi.component.group.entity;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public interface IGroupMembership {

    Long getGroupId();

    Long getUserId();

    GroupMembershipRole getRole();

    String getDisplayName();

}
