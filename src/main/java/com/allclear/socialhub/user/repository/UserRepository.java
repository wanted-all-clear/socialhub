package com.allclear.socialhub.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allclear.socialhub.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
}
