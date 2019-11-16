package com.mistermicheels.cymi.web.api.controller.authentication.input;

import javax.validation.constraints.NotNull;

public class SignupInput {
    
    @NotNull
    public String email;
    
    @NotNull
    public String password;
    
    @NotNull
    public String defaultDisplayName;

}
