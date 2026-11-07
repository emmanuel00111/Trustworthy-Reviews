package com.sysc4806.reviews.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Plain unit tests for {@link User} — no Spring context or database needed. */
class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void constructorAndGettersWork() {
        User user = new User("alice", "alice@example.com");

        assertNull(user.getId(), "ID is only assigned once saved");
        assertEquals("alice", user.getUsername());
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    void settersUpdateFields() {
        User user = new User("alice", "alice@example.com");
        user.setUsername("alice2");
        user.setEmail("alice2@example.com");

        assertEquals("alice2", user.getUsername());
        assertEquals("alice2@example.com", user.getEmail());
    }

    @Test
    void validUserHasNoViolations() {
        assertTrue(validator.validate(new User("alice", "alice@example.com")).isEmpty());
    }

    @Test
    void blankUsernameIsInvalid() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("", "alice@example.com"));
        assertFalse(violations.isEmpty());
    }

    @Test
    void badEmailIsInvalid() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User("alice", "not-an-email"));
        assertFalse(violations.isEmpty());
    }

    @Test
    void unsavedUsersAreNotEqual() {
        assertNotEquals(new User("a", "a@example.com"), new User("a", "a@example.com"));
    }
}
