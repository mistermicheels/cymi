package com.mistermicheels.cymi.component.group;

public interface GroupUserLink {

    Group getGroup();

    Long getGroupId();

    Long getUserId();

    GroupMembershipRole getRole();

}
