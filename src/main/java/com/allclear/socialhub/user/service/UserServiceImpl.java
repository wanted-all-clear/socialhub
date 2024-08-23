package com.allclear.socialhub.user.service;

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repo;

	public void userLogin(User user) {
	}

	public User checkUsername(String username) {
		User user = repo.findByUsername(username);

		if (user == null) {
			throw new RuntimeException(ErrorCode.USER_NOT_EXIST.getMessage());
		}

		return user;
	}
}
