package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();
    private final String examplePassword = "examplePassword";

    private final String hashedExamplePassword = this.passwordService
            .getSaltedPasswordHash(this.examplePassword);

    @Test()
    public void PasswordCheckFailsForIncorrectPassword() {
        assertFalse(this.passwordService.isValidPassword("incorrectPassword",
                this.hashedExamplePassword));
    }

    @Test
    public void PasswordCheckSucceedsForCorrectPassword() {
        assertTrue(this.passwordService.isValidPassword(this.examplePassword,
                this.hashedExamplePassword));
    }

}
