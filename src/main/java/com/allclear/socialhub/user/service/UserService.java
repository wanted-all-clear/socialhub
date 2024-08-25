package com.allclear.socialhub.user.service;

import org.springframework.stereotype.Service;

import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;

@Service
public interface UserService {

	void joinUser(UserJoinRequest request);

	User userLogin(UserLoginRequest request);

	String userDuplicateCheck(String username);
}
