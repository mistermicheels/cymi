package com.mistermicheels.cymi.web.api.controller.authentication.input;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

public class SignupInput {

    @NotNull
    public String email;

    @NotNull
    public String password;

    @NotNull
    public String defaultDisplayName;

    @Nullable
    public String emailConfirmationToken;

}
