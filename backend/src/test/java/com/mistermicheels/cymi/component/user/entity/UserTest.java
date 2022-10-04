package com.mistermicheels.cymi.component.user.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.user.entity.User;

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
