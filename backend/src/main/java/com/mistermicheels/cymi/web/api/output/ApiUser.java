package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.user.User;

public class ApiUser {
    
    private final Long id;
    private final String email;
    private final String defaultDisplayName;
    
    public ApiUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.defaultDisplayName = user.getDefaultDisplayName().get();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }
    
    

}
