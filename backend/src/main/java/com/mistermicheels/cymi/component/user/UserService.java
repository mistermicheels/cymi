package com.mistermicheels.cymi.component.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.user.entity.IUser;
import com.mistermicheels.cymi.component.user.entity.User;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserSignupService userSignupService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    UserService(UserRepository repository, UserSignupService userSignupService,
            UserAuthenticationService userAuthenticationService) {
        this.repository = repository;
        this.userSignupService = userSignupService;
        this.userAuthenticationService = userAuthenticationService;
    }

    public void signUpUser(LoginData loginData, String defaultDisplayName) {
        this.userSignupService.signUpUser(loginData, defaultDisplayName);
    }

    public void signUpUser(LoginData loginData, String defaultDisplayName,
            @Nullable String emailConfirmationToken) {
        this.userSignupService.signUpUser(loginData, defaultDisplayName, emailConfirmationToken);
    }

    public void confirmEmail(String emailConfirmationToken) {
        this.userSignupService.confirmEmail(emailConfirmationToken);
    }

    public SessionDataOutgoing getSessionDataForLogin(LoginData loginData) {
        return this.userAuthenticationService.getSessionDataForLogin(loginData);
    }

    public Long getAuthenticatedUserId(SessionDataIncoming sessionData) {
        return this.userAuthenticationService.getAuthenticatedUserId(sessionData);
    }

    public IUser findByIdOrThrow(Long id) {
        return this.repository.findById(id).orElseThrow(
                () -> new InvalidRequestException("There is no user with the specified ID"));
    }

    public User getReference(Long id) {
        return this.repository.getOne(id);
    }

    public IUser findByEmailOrInviteNew(String email, String invitingGroupName) {
        String emailLowerCase = email.toLowerCase();

        // any race conditions will be taken care of by the unique email index
        return this.repository.findByEmail(emailLowerCase).orElseGet(
                () -> this.userSignupService.inviteUser(emailLowerCase, invitingGroupName));
    }

}
