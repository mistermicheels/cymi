package com.mistermicheels.cymi.web.api.controller.authentication.input;

import javax.validation.constraints.NotNull;

public class LoginInput {
    
    @NotNull
    public String email;
    
    @NotNull
    public String password;

}
