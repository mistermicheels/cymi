package com.mistermicheels.cymi.component.group;

interface GroupUserLink {

    Group getGroup();

    Long getGroupId();

    Long getUserId();

    GroupMembershipRole getRole();

}
