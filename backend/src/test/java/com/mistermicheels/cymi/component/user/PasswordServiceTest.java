package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

public class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();
    private final String examplePassword = "examplePassword";
    
    private final String hashedExamplePassword = this.passwordService
            .getSaltedPasswordHash(this.examplePassword);

    @Test()
    public void PasswordCheckFailsForIncorrectPassword() {
        assertThrows(InvalidRequestException.class, () -> this.passwordService
                .checkPassword("incorrectPassword", this.hashedExamplePassword));
    }

    @Test
    public void PasswordCheckSucceedsForCorrectPassword() {
        this.passwordService.checkPassword(this.examplePassword, this.hashedExamplePassword);
    }

}
