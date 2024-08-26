package com.allclear.socialhub.user.repository;

import com.allclear.socialhub.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    Optional<User> findByEmailAndUsername(String email, String username);

}
