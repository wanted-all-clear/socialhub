package com.allclear.socialhub.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.controller.UserController;
import com.allclear.socialhub.user.dto.UserEmailRequest;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.service.EmailService;
import com.allclear.socialhub.user.service.UserService;
import com.allclear.socialhub.user.type.EmailType;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    /**
     * 로그인 통합 테스트
     * 작성자 : 김은정
     */
    @Test
    public void 사용자_로그인_테스트() {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username("user1")
                .password("abcd1234..").build();

        HttpEntity<UserLoginRequest> httpEntity = new HttpEntity<>(userLoginRequest);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
                httpEntity, String.class);

        String jwtToken = responseEntity.getHeaders().getFirst("AUTHORIZATION");
        jwtTokenProvider.extractAllClaims(jwtToken);
    }

    @Test
    void sendEmailVerification_Success() throws MessagingException {
        // given
        doNothing().when(emailService).sendEmail(anyString(), any(EmailType.class));

        // when
        ResponseEntity<String> response = userController.sendEmailVerification();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("이메일로 인증 코드가 전송되었습니다.", response.getBody());
    }

    @Test
    void sendEmailVerification_Failure() throws MessagingException {
        // given
        doThrow(new MessagingException("이메일 전송 실패")).when(emailService).sendEmail(anyString(), any(EmailType.class));

        // when & then
        try {
            userController.sendEmailVerification();
        } catch (MessagingException e) {
            assertEquals("이메일 전송 실패", e.getMessage());
        }
    }

    @Test
    void verifyEmailCode_Success() {
        // given
        UserEmailRequest request = new UserEmailRequest("user@example.com", "123456");

        // emailService.getVerificationToken()이 올바른 인증 코드를 반환하도록 모킹 설정
        when(emailService.getVerificationToken(request.getEmail())).thenReturn("123456");

        // userService.verifyUser()가 true를 반환하도록 모킹 설정
        when(userService.verifyUser("123456", "123456", "user@example.com")).thenReturn(true);

        // when
        ResponseEntity<String> response = userController.verifyEmailCode(request);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("이메일 인증이 완료되었습니다.", response.getBody());
    }


    @Test
    void verifyEmailCode_Failure() {
        // given
        UserEmailRequest request = new UserEmailRequest("user@example.com", "123456");
        when(emailService.getVerificationToken(request.getEmail())).thenReturn("wrongCode");
        when(userService.verifyUser(anyString(), anyString(), anyString())).thenReturn(false);

        // when
        ResponseEntity<String> response = userController.verifyEmailCode(request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("인증 코드가 일치하지 않거나 만료되었습니다.", response.getBody());
    }

    @Test
    void joinUser_Success() {
        // given
        UserJoinRequest request = new UserJoinRequest("username", "user@example.com", "password");
        doNothing().when(userService).joinUser(request);

        // when
        ResponseEntity<String> response = userController.joinUser(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("회원가입이 완료되었습니다.", response.getBody());
    }

    @Test
    void joinUser_Failure() {
        // given
        UserJoinRequest request = new UserJoinRequest("username", "user@example.com", "password");
        doThrow(new CustomException(ErrorCode.EMAIL_DUPLICATION)).when(userService).joinUser(request);

        // when & then
        try {
            userController.joinUser(request);
        } catch (CustomException e) {
            assertEquals(ErrorCode.EMAIL_DUPLICATION.getMessage(), e.getMessage());
            assertEquals(HttpStatus.CONFLICT, e.getErrorCode().getHttpStatus());
        }
    }

}
