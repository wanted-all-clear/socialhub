package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.dto.UserJoinRequest;

public interface UserService {

    void joinUser(UserJoinRequest request);

    boolean verifyUser(String storedCode, String authCode);

}
