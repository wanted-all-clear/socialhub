package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void joinUser(UserJoinRequest request);

    User userLogin(UserLoginRequest request);

}
