package com.allclear.socialhub.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("")
	public ResponseEntity<String> joinUser(@Valid @RequestBody UserJoinRequest request) {

		try {
			userService.joinUser(request);
			return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
		} catch (CustomException e) {
			throw e;
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> userLogin(@RequestBody UserLoginRequest request) {
		User user = userService.userLogin(request);
		String jwtToken = jwtTokenProvider.createToken(user);

		return ResponseEntity.ok().header("AUTHORIZATION", jwtToken).body("로그인이 완료되었습니다.");
	}

	@PostMapping("/duplicate-check")
	public ResponseEntity<String> userDuplicateCheck(@RequestBody String username) {
		String message = userService.userDuplicateCheck(username);
		return ResponseEntity.ok(message);
	}

}
