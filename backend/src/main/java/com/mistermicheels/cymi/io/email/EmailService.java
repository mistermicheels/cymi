package com.mistermicheels.cymi.io.email;

import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.io.email.emailMessage.EmailMessage;

@Service
public class EmailService {
    
    public void send(EmailMessage emailMessage) {
        System.out.println("Email to " + emailMessage.getRecipient() + ": [" + emailMessage.getBody() + "]");
    }

}
