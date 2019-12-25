package com.mistermicheels.cymi.io.email.emailMessage;

public class InvitationEmailMessage implements EmailMessage {

    private final String recipient;
    private final String groupName;
    private final String emailConfirmationToken;

    public InvitationEmailMessage(String recipient, String groupName,
            String emailConfirmationToken) {
        this.recipient = recipient;
        this.groupName = groupName;
        this.emailConfirmationToken = emailConfirmationToken;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public String getBody() {
        return "ACCEPT INVITATION: " + this.groupName + " " + this.emailConfirmationToken;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getEmailConfirmationToken() {
        return this.emailConfirmationToken;
    }

}
