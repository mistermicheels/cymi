package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.config.security.SecurityProperties;
import com.mistermicheels.cymi.io.email.EmailService;
import com.mistermicheels.cymi.io.email.emailMessage.ConfirmEmailEmailMessage;

@Service
class UserSignupService {

    private final UserRepository repository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;

    private final int emailConfirmationTokenValidityDays;

    @Autowired
    UserSignupService(UserRepository repository,
            EmailConfirmationTokenRepository emailConfirmationTokenRepository, PasswordService passwordService,
            EmailService emailService, SecurityProperties securityProperties) {
        this.repository = repository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.passwordService = passwordService;
        this.emailService = emailService;
        this.emailConfirmationTokenValidityDays = securityProperties.getEmailConfirmationTokenValidityDays();
    }

    @Transactional
    public void signUpUser(LoginData loginData, String defaultDisplayName) {
        String email = loginData.getEmail();
        this.checkEmailUnique(email);

        User user = new User(email);
        String password = loginData.getPassword();
        String saltedPasswordHash = this.passwordService.getSaltedPasswordHash(password);
        user.signup(saltedPasswordHash, defaultDisplayName);

        // save and flush so the user gets its DB-generated ID
        this.repository.saveAndFlush(user);
        
        EmailConfirmationToken emailConfirmationToken = this.getNewEmailConfirmationToken(user);        
        this.emailConfirmationTokenRepository.save(emailConfirmationToken);

        ConfirmEmailEmailMessage emailMessage = new ConfirmEmailEmailMessage(email, emailConfirmationToken.getId(),
                user.getId());        
        
        this.emailService.send(emailMessage);
    }

    private void checkEmailUnique(String email) {
        // ignores possible (but unlikely) race conditions
        // those will be caught by the unique index

        Optional<User> existingWithEmail = this.repository.findByEmail(email);

        if (existingWithEmail.isPresent()) {
            throw new InvalidRequestException("There is already a user with the same email address",
                    InvalidRequestExceptionType.EmailAlreadyTaken);
        }
    }

    private EmailConfirmationToken getNewEmailConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ZonedDateTime expirationTimestamp = ZonedDateTime.now().plusDays(this.emailConfirmationTokenValidityDays);
        return new EmailConfirmationToken(token, user, expirationTimestamp);
    }

    @Transactional
    public void confirmEmail(String emailConfirmationToken, Long userId) {
        String invalidTokenMessage = "Invalid email confirmation token";

        EmailConfirmationToken token = this.emailConfirmationTokenRepository.findByIdWithUser(emailConfirmationToken)
                .orElseThrow(() -> new InvalidRequestException(invalidTokenMessage));

        if (token.hasExpired()) {
            throw new InvalidRequestException(invalidTokenMessage);
        }

        User userFromToken = token.getUser();

        if (!userFromToken.getId().equals(userId)) {
            throw new InvalidRequestException(invalidTokenMessage);
        }

        userFromToken.confirmEmail();
        this.repository.save(userFromToken);
        this.emailConfirmationTokenRepository.delete(token);
    }

}
