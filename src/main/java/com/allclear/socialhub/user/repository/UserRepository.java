package com.allclear.socialhub.user.repository;

import com.allclear.socialhub.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
