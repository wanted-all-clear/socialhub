package com.allclear.socialhub.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.allclear.socialhub.common.config.WebSecurityConfig;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserLoginDto;
import com.allclear.socialhub.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repo;
	private final WebSecurityConfig securityConfig;

	/**
	 * 1. 로그인
	 * 작성자 : 김은정
	 * @param loginDto
	 */
	public void userLogin(UserLoginDto loginDto) {
		try {
			User user = checkUsername(loginDto.getUsername());
			checkPassword(user, loginDto.getPassword());
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 2. 아이디 확인
	 * 작성자 : 김은정
	 * @param username
	 * @return User user
	 */
	public User checkUsername(String username) {
		User user = repo.findByUsername(username);

		if (user == null) {
			throw new RuntimeException(ErrorCode.USER_NOT_EXIST.getMessage());
		}

		return user;
	}

	/**
	 * 3. 비밀번호 확인
	 * 작성자 : 김은정
	 * @param user
	 * @param password
	 */
	public void checkPassword(User user, String password) {
		PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException(ErrorCode.PASSWORD_NOT_VALID.getMessage());
		}
	}
}
