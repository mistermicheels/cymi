package com.mistermicheels.cymi.component.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

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

    public void confirmEmail(String emailConfirmationToken, Long userId) {
        this.userSignupService.confirmEmail(emailConfirmationToken, userId);
    }

    public SessionDataOutgoing getSessionDataForLogin(LoginData loginData) {
        return this.userAuthenticationService.getSessionDataForLogin(loginData);
    }

    public Long getAuthenticatedUserId(SessionDataIncoming sessionData) {
        return this.userAuthenticationService.getAuthenticatedUserId(sessionData);
    }

    public User findByIdOrThrow(Long id) {
        return this.repository.findById(id).orElseThrow(
                () -> new InvalidRequestException("There is no user with the specified ID"));
    }

    public User getReference(Long id) {
        return this.repository.getOne(id);
    }

    public Optional<User> findByEmail(String email) {
        String emailLowerCase = email.toLowerCase();
        return this.repository.findByEmail(emailLowerCase);
    }

}
