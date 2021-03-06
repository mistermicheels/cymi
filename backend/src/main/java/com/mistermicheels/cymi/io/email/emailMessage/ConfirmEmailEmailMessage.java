package com.mistermicheels.cymi.io.email.emailMessage;

public class ConfirmEmailEmailMessage implements EmailMessage {

    private final String recipient;
    private final String emailConfirmationToken;

    public ConfirmEmailEmailMessage(String recipient, String emailConfirmationToken) {
        this.recipient = recipient;
        this.emailConfirmationToken = emailConfirmationToken;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public String getBody() {
        return "CONFIRM EMAIL: " + this.emailConfirmationToken;
    }

    public String getEmailConfirmationToken() {
        return this.emailConfirmationToken;
    }

}
