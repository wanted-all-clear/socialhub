package com.allclear.socialhub.user;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.controller.UserController;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserEmailRequest;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.service.EmailService;
import com.allclear.socialhub.user.service.UserService;
import com.allclear.socialhub.user.type.EmailType;
import com.allclear.socialhub.user.type.UsernameDupStatus;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private Claims mockClaims;

    @InjectMocks
    private UserController userController;

    public String username = "popcorn23";
    public String email = "wpdls879@gmail.com";
    public String password = "qlalfqjsghgh23";
    private User user;
    private String jwt;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        // 회원가입
        UserJoinRequest userJoinRequest = new UserJoinRequest(username, email, password);

        HttpEntity<UserJoinRequest> httpEntityJoin = new HttpEntity<>(userJoinRequest);
        testRestTemplate.exchange("/api/users", HttpMethod.POST, httpEntityJoin, String.class);

        // User 객체 사용해 JWT 토큰 생성
        user = User.builder()
                .id(1L)
                .email("fkznsha23@gmail.com")
                .password(password)
                .username(username).build();

        // jwtTokenProvider를 사용해 JWT 토큰 생성
        jwt = jwtTokenProvider.createToken(user);
    }

    @Test
    @DisplayName("사용자가 로그인할 때 성공적으로 JWT 토큰을 발급받는지 테스트합니다.")
    public void 사용자_로그인_테스트() {
        // 로그인
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username(username)
                .password(password).build();

        HttpEntity<UserLoginRequest> httpEntity = new HttpEntity<>(userLoginRequest);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
                httpEntity, String.class);

        String jwtToken = responseEntity.getHeaders().getFirst("AUTHORIZATION");
        Claims token = jwtTokenProvider.extractAllClaims(jwtToken);
        String email = jwtTokenProvider.extractEmail(token);
        String tokenStr = jwtTokenProvider.extractUsername(token);

        assertThat(userLoginRequest.getUsername()).isEqualTo(tokenStr);
        assertThat(this.email).isEqualTo(email);
    }

    @Test
    @DisplayName("회원가입 시 중복된 계정이 있을 경우 예외가 발생하는지 테스트합니다.")
    public void 계정_중복인_경우_테스트() {
        // 계정 중복 확인
        HttpEntity<String> httpEntity = new HttpEntity<>(username);
        ResponseEntity<String> result = testRestTemplate.exchange("/api/users/duplicate-check", HttpMethod.POST,
                httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(ErrorCode.USERNAME_DUPLICATION.getHttpStatus());
    }

    @Test
    @DisplayName("회원가입 시 중복된 계정이 없을 경우 성공적으로 처리되는지 테스트합니다.")
    public void 계정_중복이_없는_경우_테스트() {

        String username = "user12";

        HttpEntity<String> httpEntity = new HttpEntity<>(username);
        ResponseEntity<String> result = testRestTemplate.exchange("/api/users/duplicate-check", HttpMethod.POST,
                httpEntity, String.class);

        assertThat(result.getBody()).isEqualTo(UsernameDupStatus.USERNAME_AVAILABLE.getMessage());
    }

    @Test
    @DisplayName("이메일 인증 코드 전송이 성공적으로 이루어지는지 테스트합니다.")
    void sendEmailVerification_Success() throws Exception {
        // given
        String validJwt = jwtTokenProvider.createToken(user);

        // JwtTokenProvider를 완전히 모킹하여 특정 메서드 스터빙
        JwtTokenProvider mockJwtTokenProvider = mock(JwtTokenProvider.class);

        when(mockJwtTokenProvider.extractAllClaims(validJwt)).thenReturn(mockClaims);
        when(mockJwtTokenProvider.extractEmail(mockClaims)).thenReturn(user.getEmail());

        doNothing().when(emailService).sendEmail(user.getEmail(), EmailType.VERIFICATION);

        mockMvc.perform(post("/api/users/email-code")
                        .header("Authorization", validJwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("이메일로 인증 코드가 전송되었습니다."));

        verify(emailService, times(1)).sendEmail(user.getEmail(), EmailType.VERIFICATION);

    }


    @Test
    @DisplayName("이메일 인증 코드 전송 시 실패하는 경우를 테스트합니다.")
    void sendEmailVerification_Failure() throws Exception {
        // given
        user = User.builder()
                .id(1L)
                .email("wpdls879@gmail.com")
                .username("popcorn23")
                .password("qlalfqjsghgh23")
                .build();

        String validJwt = jwtTokenProvider.createToken(user);

        // 이메일 전송 시 예외가 발생하도록 설정
        doThrow(new MessagingException("이메일 전송 실패")).when(emailService).sendEmail(user.getEmail(), EmailType.VERIFICATION);

        // when & then
        mockMvc.perform(post("/api/users/email-code")
                        .header("Authorization", validJwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // 예외 발생 시 500 에러를 반환할 것으로 예상
                .andExpect(content().string("이메일 전송 실패"));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 시 실패하는 경우를 테스트합니다.")
    void verifyEmailCode_Failure() {
        // given
        String token = "testToken"; // 테스트용 JWT 토큰
        String email = "user@example.com"; // 테스트용 이메일
        String authCode = "123456"; // 테스트용 인증 코드
        UserEmailRequest request = new UserEmailRequest(authCode); // 인증 코드 요청 객체 생성

        // JwtTokenProvider를 완전히 모킹하여 특정 메서드 스터빙
        JwtTokenProvider mockJwtTokenProvider = mock(JwtTokenProvider.class);

        // JWT 토큰에서 이메일을 추출하도록 모킹 설정
        when(mockJwtTokenProvider.extractEmail(any(Claims.class))).thenReturn(email);
        when(mockJwtTokenProvider.extractAllClaims(token)).thenReturn(mockClaims);

        // emailService.getVerificationToken()이 잘못된 인증 코드를 반환하도록 모킹 설정
        when(emailService.getVerificationToken(email)).thenReturn("wrongCode");

        // userService.verifyUser()가 false를 반환하도록 모킹 설정
        when(userService.verifyUser("wrongCode", authCode, email)).thenReturn(false);

        // JwtTokenProvider를 사용한 UserController 인스턴스 생성
        UserController userController = new UserController(emailService, userService, mockJwtTokenProvider);

        // when
        ResponseEntity<String> response = userController.verifyEmailCode(token, request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // 상태 코드가 400 BAD_REQUEST인지 확인
        assertEquals("인증 코드가 일치하지 않거나 만료되었습니다.", response.getBody()); // 응답 메시지 확인
    }


    @Test
    @DisplayName("회원가입이 성공적으로 처리되는지 테스트합니다.")
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
    @DisplayName("회원가입 시 이메일 중복으로 인해 실패하는 경우를 테스트합니다.")
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
