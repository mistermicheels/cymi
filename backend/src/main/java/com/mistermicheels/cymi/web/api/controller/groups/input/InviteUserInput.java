package com.mistermicheels.cymi.web.api.controller.groups.input;

import javax.validation.constraints.NotNull;

import com.mistermicheels.cymi.component.group.GroupMembershipRole;

public class InviteUserInput {

    @NotNull
    public String email;

    @NotNull
    public GroupMembershipRole role;

}
