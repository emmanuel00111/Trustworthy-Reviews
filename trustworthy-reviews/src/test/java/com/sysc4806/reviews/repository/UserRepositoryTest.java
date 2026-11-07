package com.sysc4806.reviews.repository;

import com.sysc4806.reviews.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link UserRepository} against the H2 in-memory database.
 * {@code @DataJpaTest} rolls back each test, so tests don't affect each other.
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAssignsIdAndUserCanBeRetrieved() {
        User saved = userRepository.save(new User("alice", "alice@example.com"));

        assertNotNull(saved.getId(), "saving should generate an ID");

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("alice", found.get().getUsername());
        assertEquals("alice@example.com", found.get().getEmail());
    }

    @Test
    void findByUsernameReturnsMatchingUser() {
        userRepository.save(new User("bob", "bob@example.com"));

        Optional<User> found = userRepository.findByUsername("bob");

        assertTrue(found.isPresent());
        assertEquals("bob@example.com", found.get().getEmail());
        assertTrue(userRepository.existsByUsername("bob"));
    }

    @Test
    void findByUsernameReturnsEmptyForUnknownUser() {
        assertTrue(userRepository.findByUsername("nobody").isEmpty());
        assertFalse(userRepository.existsByUsername("nobody"));
    }

    @Test
    void findAllReturnsEverySavedUser() {
        userRepository.save(new User("carol", "carol@example.com"));
        userRepository.save(new User("dave", "dave@example.com"));

        assertEquals(2, userRepository.count());
    }

    @Test
    void duplicateUsernameIsRejected() {
        userRepository.save(new User("erin", "erin@example.com"));
        entityManager.flush();

        // IDENTITY ids make Hibernate INSERT immediately on save(), so the unique
        // constraint fails there and Spring translates it to DataIntegrityViolationException.
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(new User("erin", "other@example.com"));
            entityManager.flush();
        });
    }
}
