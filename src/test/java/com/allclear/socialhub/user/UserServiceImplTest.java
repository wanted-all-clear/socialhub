package com.allclear.socialhub.user;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.exception.DuplicateUserInfoException;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserJoinRequest request;

    private UserLoginRequest loginRequest;


    @BeforeEach
    public void setUp() {

        loginRequest = new UserLoginRequest("validUser", "password");

        request = new UserJoinRequest();
        request.setUsername("validUser");
        request.setEmail("valid@example.com");
        request.setPassword("ValidPass123!");
    }


    @Test
    @DisplayName("로그인 시 사용자가 전달한 계정과 일치하는 계정이 있는 경우")
    public void checkUsernameSuccessTest() {
        // given
        given(userRepository.findByUsername(loginRequest.getUsername())).willReturn(mock(User.class));

        // when
        userService.checkUsername(loginRequest.getUsername());

        // then
        verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
    }


    @Test
    @DisplayName("로그인 시 사용자 전달한 계정과 일치하지 않는 경우를 테스트합니다.")
    public void checkUsernameFailTest() {
        // given
        String uername = loginRequest.getUsername();

        //when
        Throwable throwable = assertThrows(RuntimeException.class,
                () -> userService.checkUsername(uername));

        // then
        verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
        assertThat(throwable.getMessage()).isEqualTo(ErrorCode.USER_NOT_EXIST.getMessage());
    }

    @Test
    @DisplayName("회원가입 시 이메일이 이미 존재하는 경우를 테스트합니다.")
    public void testJoinUserEmailAlreadyExists() {
        // given
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when & then
        assertThrows(DuplicateUserInfoException.class, () -> userService.joinUser(request));

        verify(userRepository, never()).save(Mockito.any(User.class));
    }


    @Test
    @DisplayName("회원가입 시 사용자명이 이미 존재하는 경우를 테스트합니다.")
    public void testJoinUserUsernameAlreadyExists() {
        // given
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // when & then
        assertThrows(DuplicateUserInfoException.class, () -> userService.joinUser(request));

        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("회원가입 시 비밀번호 길이 유효하지 않은 경우를 테스트합니다.")
    public void testJoinUserInvalidPasswordLength() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("newUser");
        request.setEmail("new@example.com");
        request.setPassword("short");

        // when & then
        assertThrows(CustomException.class, () -> userService.joinUser(request));

        verify(userRepository, never()).save(Mockito.any(User.class));
    }


    @Test
    @DisplayName("회원가입 시 비밀번호 조건에 두 가지 이상이 포함되지 않은 경우를 테스트합니다.")
    public void testJoinUserInvalidPasswordPattern() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("newUser");
        request.setEmail("new@example.com");
        request.setPassword("onlyletters");

        // when & then
        assertThrows(CustomException.class, () -> userService.joinUser(request));

        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("회원가입 시 사용하고자 하는 계정을 이미 다른 사용자가 사용한 경우를 테스트합니다.")
    public void duplicateAccountExistsTest() {
        //given

    }

}
