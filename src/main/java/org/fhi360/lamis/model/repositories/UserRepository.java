package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    User findByUsername(String username);
}
