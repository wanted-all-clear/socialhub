package com.allclear.socialhub.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.controller.UserController;
import com.allclear.socialhub.user.dto.UserEmailRequest;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.service.EmailService;
import com.allclear.socialhub.user.service.UserService;
import com.allclear.socialhub.user.type.EmailType;
import com.allclear.socialhub.user.type.UsernameDupStatus;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	@Mock
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
		String token = jwtTokenProvider.extractAllClaims(jwtToken).getSubject();
		String[] tokenArray = token.split(",");

		assertThat(userLoginRequest.getUsername()).isEqualTo(tokenArray[1]);
	}

	@Test
	public void 계정_중복인_경우_테스트() {
		String username = "user1";

		HttpEntity<String> httpEntity = new HttpEntity<>(username);
		ResponseEntity<String> result = testRestTemplate.exchange("/api/users/duplicate-check", HttpMethod.POST,
				httpEntity, String.class);

		assertThat(result.getBody()).isEqualTo(UsernameDupStatus.USERNAME_ALREADY_TAKEN.getMessage());
	}

	@Test
	public void 계정_중복이_없는_경우_테스트() {
		String username = "user1234";

		HttpEntity<String> httpEntity = new HttpEntity<>(username);
		ResponseEntity<String> result = testRestTemplate.exchange("/api/users/duplicate-check", HttpMethod.POST,
				httpEntity, String.class);

		assertThat(result.getBody()).isEqualTo(UsernameDupStatus.USERNAME_AVAILABLE.getMessage());
	}

	@Test
	void sendEmailVerification_Success() throws MessagingException {
		// given
		// 테스트용 JWT 토큰 생성
		String token = Jwts.builder()
				.setSubject("wpdls879@gmail.com")
				.signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
				.compact();

		// jwtTokenProvider의 extractEmailFromToken 메서드가 "wpdls879@gmail.com"을 반환하도록 모킹
		when(jwtTokenProvider.extractEmailFromToken(anyString())).thenReturn("wpdls879@gmail.com");

		// emailService의 sendEmail 메서드가 호출되더라도 아무 동작도 하지 않도록 모킹
		doNothing().when(emailService).sendEmail(anyString(), any(EmailType.class));

		// when
		// Authorization 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		// UserController의 sendEmailVerification 메서드 호출
		ResponseEntity<String> response = userController.sendEmailVerification(headers.getFirst("Authorization"));

		// then
		// 상태 코드와 응답 메시지가 기대한 대로 반환되는지 확인
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("이메일로 인증 코드가 전송되었습니다.", response.getBody());
	}


	@Test
	void sendEmailVerification_Failure() throws MessagingException {
		// given
		String token = "testToken"; // 테스트용 JWT 토큰
		// emailService의 sendEmail 메서드가 호출될 때 MessagingException을 던지도록 설정
		doThrow(new MessagingException("이메일 전송 실패")).when(emailService).sendEmail(anyString(), any(EmailType.class));

		// when & then
		try {
			// 헤더를 포함한 HttpHeaders 객체 생성
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			// HttpEntity 객체 생성 (헤더를 포함한 상태)
			HttpEntity<String> entity = new HttpEntity<>(headers);

			// UserController의 sendEmailVerification 메서드를 호출하여 예외 발생 여부를 확인
			ResponseEntity<String> response = userController.sendEmailVerification(headers.getFirst("Authorization"));
		} catch (MessagingException e) {
			// 예외가 발생했을 때 예외 메시지가 "이메일 전송 실패"와 일치하는지 확인
			assertEquals("이메일 전송 실패", e.getMessage());
		}
	}


	@Test
	void verifyEmailCode_Failure() {
		// given
		String token = "testToken"; // 테스트용 JWT 토큰
		String email = "user@example.com"; // 테스트용 이메일
		String authCode = "123456"; // 테스트용 인증 코드
		UserEmailRequest request = new UserEmailRequest(authCode); // 인증 코드 요청 객체 생성

		// JWT 토큰에서 이메일을 추출하도록 모킹 설정
		when(jwtTokenProvider.extractEmailFromToken(token)).thenReturn(email);

		// emailService.getVerificationToken()이 잘못된 인증 코드를 반환하도록 모킹 설정
		when(emailService.getVerificationToken(email)).thenReturn("wrongCode");

		// userService.verifyUser()가 false를 반환하도록 모킹 설정
		when(userService.verifyUser("wrongCode", authCode, email)).thenReturn(false);

		// when
		ResponseEntity<String> response = userController.verifyEmailCode(token, request);

		// then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // 상태 코드가 400 BAD_REQUEST인지 확인
		assertEquals("인증 코드가 일치하지 않거나 만료되었습니다.", response.getBody()); // 응답 메시지 확인
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
