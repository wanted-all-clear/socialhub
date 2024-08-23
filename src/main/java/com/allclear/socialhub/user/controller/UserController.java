package com.allclear.socialhub.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allclear.socialhub.user.dto.UserLoginDto;
import com.allclear.socialhub.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService service;

	@PostMapping("/login")
	public ResponseEntity<String> userLogin(UserLoginDto loginDto) {

		return null;
	}

}
