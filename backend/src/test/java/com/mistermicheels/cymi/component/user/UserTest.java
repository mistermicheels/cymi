package com.mistermicheels.cymi.component.user;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

public class UserTest {

    @Test
    public void CreateFailsForInvalidEmail() {
        assertThrows(InvalidRequestException.class, () -> new User("invalid"));
    }

    @Test
    public void CreateFailsForTooLongEmail() {
        assertThrows(InvalidRequestException.class, () -> new User("a".repeat(256) + "@email.com"));
    }

    @Test
    public void CreateSucceedsForValidEmail() {
        new User("email@email.com");
    }

}
