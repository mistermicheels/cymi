package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

public class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();
    private final String examplePassword = "examplePassword";
    private final String hashedExamplePassword = passwordService.getSaltedPasswordHash(examplePassword);

    @Test()
    public void PasswordCheckFailsForIncorrectPassword() {
        assertThrows(InvalidRequestException.class,
                () -> passwordService.checkPassword("incorrectPassword", hashedExamplePassword));
    }

    @Test
    public void PasswordCheckSucceedsForCorrectPassword() {
        passwordService.checkPassword(examplePassword, hashedExamplePassword);
    }

}
