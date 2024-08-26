package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserInfoUpdateRequest;
import com.allclear.socialhub.user.dto.UserInfoUpdateResponse;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;

public interface UserService {

    void joinUser(UserJoinRequest request);

    boolean verifyUser(String storedCode, String authCode, String email);
    

    UserInfoUpdateResponse updateUserInfo(UserInfoUpdateRequest request, String token);

    User userLogin(UserLoginRequest request);

    String userDuplicateCheck(String username);

}
