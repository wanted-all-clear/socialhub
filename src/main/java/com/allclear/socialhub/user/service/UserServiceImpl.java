package com.allclear.socialhub.user.service;

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.exception.DuplicateUserInfoException;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void joinUser(UserJoinRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserInfoException(ErrorCode.EMAIL_DUPLICATION);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserInfoException(ErrorCode.USERNAME_DUPLICATION);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .status(UserStatus.ACTIVE)
                .certifyStatus(UserCertifyStatus.UNAUTHENTICATED)
                .build();

        userRepository.save(user);

    }

}
