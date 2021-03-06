package com.mistermicheels.cymi.component.user;

public class LoginData {

    private final String email;
    private final String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmailLowerCase() {
        return this.email.toLowerCase();
    }

    public String getPassword() {
        return this.password;
    }

}
