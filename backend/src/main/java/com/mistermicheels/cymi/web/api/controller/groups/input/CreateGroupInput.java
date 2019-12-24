package com.mistermicheels.cymi.web.api.controller.groups.input;

import javax.validation.constraints.NotNull;

public class CreateGroupInput {

    @NotNull
    public String name;

    @NotNull
    public String userDisplayName;

}
