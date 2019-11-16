package com.mistermicheels.cymi.config.security;

import java.util.Collections;

/**
 * Custom UserDetails implementation that contains the user's ID.
 */
@SuppressWarnings("serial")
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public CustomUserDetails(Long authenticatedUserId) {
        super("placeholder", "placeholder", true, true, true, true, Collections.emptyList());
        this.id = authenticatedUserId;
    }

    public Long getId() {
        return this.id;
    }

}
