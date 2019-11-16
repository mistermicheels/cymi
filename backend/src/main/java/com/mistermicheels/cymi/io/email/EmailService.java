package com.mistermicheels.cymi.io.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.io.email.emailMessage.EmailMessage;

@Service
public class EmailService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void send(EmailMessage emailMessage) {
        logger.info("Email to " + emailMessage.getRecipient() + ": [" + emailMessage.getBody() + "]");
    }

}
