package com.mistermicheels.cymi.io.email.emailMessage;

public class ConfirmEmailEmailMessage implements EmailMessage {

    private final String recipient;
    private final String emailConfirmationToken;
    private final Long userId;

    public ConfirmEmailEmailMessage(String recipient, String emailConfirmationToken, Long userId) {
        this.recipient = recipient;
        this.emailConfirmationToken = emailConfirmationToken;
        this.userId = userId;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public String getBody() {
        return "CONFIRM EMAIL: " + this.emailConfirmationToken + " " + this.userId;
    }

    public String getEmailConfirmationToken() {
        return this.emailConfirmationToken;
    }

    public Long getUserId() {
        return this.userId;
    }

}
