package com.mistermicheels.cymi.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mistermicheels.cymi.component.user.UserTokenCleanupService;

@Component
class CleanUpExpiredUserTokens {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final UserTokenCleanupService userTokenCleanupService;    

    @Autowired
    CleanUpExpiredUserTokens(UserTokenCleanupService userTokenCleanupService) {
        this.userTokenCleanupService = userTokenCleanupService;
    }
    
    // cannot get fixedRateString from injected properties class instance
    // only constants can be provided
    @Scheduled(fixedRateString = "${app.scheduled.clean_up_expired_tokens_rate_seconds}000")
    public void execute() {
        logger.info("Cleaning up expired user tokens");
        this.userTokenCleanupService.cleanUpExpiredTokens();
    }

}
