package com.sysc4806.reviews.repository;

import com.sysc4806.reviews.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}.
 * Spring generates the implementation at runtime, so no EntityManager code is needed (see Lab 3).
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
}
