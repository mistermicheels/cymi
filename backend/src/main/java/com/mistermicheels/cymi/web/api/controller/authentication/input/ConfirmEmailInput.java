package com.mistermicheels.cymi.web.api.controller.authentication.input;

import javax.validation.constraints.NotNull;

public class ConfirmEmailInput {

    @NotNull
    public String token;

    @NotNull
    public Long userId;

}
