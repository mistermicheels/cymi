package com.mistermicheels.cymi.component.group.entity;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;

interface GroupUserLink {

    Group getGroup();

    Long getGroupId();

    Long getUserId();

    GroupMembershipRole getRole();

}
