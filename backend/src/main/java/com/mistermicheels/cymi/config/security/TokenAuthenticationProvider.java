package com.mistermicheels.cymi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.user.SessionDataIncoming;
import com.mistermicheels.cymi.component.user.UserService;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserService userService;

    @Autowired
    public TokenAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected UserDetails retrieveUser(String userName,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        SessionDataIncoming sessionData = (SessionDataIncoming) usernamePasswordAuthenticationToken
                .getCredentials();

        try {
            Long authenticatedUserId = this.userService.getAuthenticatedUserId(sessionData);
            return new CustomUserDetails(authenticatedUserId);
        } catch (InvalidRequestException exception) {
            throw new CustomAuthenticationException(exception.getMessage(), exception);
        }
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        // no additional checks needed
        return;
    }
}
