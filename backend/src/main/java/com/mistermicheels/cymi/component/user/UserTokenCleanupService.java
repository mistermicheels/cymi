package com.mistermicheels.cymi.component.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenCleanupService {

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final SessionTokenRepository sessionTokenRepository;

    @Autowired
    UserTokenCleanupService(EmailConfirmationTokenRepository emailConfirmationTokenRepository,
            SessionTokenRepository sessionTokenRepository) {
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public void cleanUpExpiredTokens() {
        this.emailConfirmationTokenRepository.deleteExpired();
        this.sessionTokenRepository.deleteExpired();
    }

}
