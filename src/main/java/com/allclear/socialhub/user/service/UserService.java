package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.dto.UserInfoUpdateRequest;
import com.allclear.socialhub.user.dto.UserInfoUpdateResponse;
import com.allclear.socialhub.user.dto.UserJoinRequest;

public interface UserService {

    void joinUser(UserJoinRequest request);

    UserInfoUpdateResponse updateUserInfo(UserInfoUpdateRequest request, String email);

}
