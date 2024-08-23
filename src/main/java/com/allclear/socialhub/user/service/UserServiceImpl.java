package com.allclear.socialhub.user.service;

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserLoginDto;
import com.allclear.socialhub.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repo;

	public void userLogin(UserLoginDto loginDto) {
		User user = checkUsername(loginDto.getUsername());
		checkPassword(user, loginDto.getPassword());
	}

	public User checkUsername(String username) {
		User user = repo.findByUsername(username);

		if (user == null) {
			throw new RuntimeException(ErrorCode.USER_NOT_EXIST.getMessage());
		}

		return user;
	}

	private void checkPassword(User user, String password) {
		if (!user.getPassword().equals(password)) {
			throw new RuntimeException(ErrorCode.PASSWORD_NOT_VALID.getMessage());
		}
	}
}
