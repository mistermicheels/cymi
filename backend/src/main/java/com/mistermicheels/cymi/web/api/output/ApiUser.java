package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.user.entity.IUser;

public class ApiUser {

    private final Long id;
    private final String email;
    private final String defaultDisplayName;

    public ApiUser(IUser user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.defaultDisplayName = user.getDefaultDisplayName().get();
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDefaultDisplayName() {
        return this.defaultDisplayName;
    }

}
