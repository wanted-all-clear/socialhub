package com.allclear.socialhub.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allclear.socialhub.common.config.WebSecurityConfig;
import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.exception.DuplicateUserInfoException;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.service.UserServiceImpl;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private WebSecurityConfig config;

	@InjectMocks
	private UserServiceImpl userService;

	private UserJoinRequest request;

	private UserLoginRequest loginRequest;

	/**
	 * 각 테스트 전에 실행되며, 테스트에 필요한 기본 데이터를 초기화합니다.
	 */
	@BeforeEach
	public void setUp() {
		loginRequest = new UserLoginRequest("validUser", "password");

		request = new UserJoinRequest();
		request.setUsername("validUser");
		request.setEmail("valid@example.com");
		request.setPassword("ValidPass123!");
	}

	/**
	 * 로그인 시 사용자가 전달한 계정과 일치하는 계정이 있는 경우
	 * 작성자 : 김은정
	 */
	@Test
	public void checkUsernameSuccessTest() {
		// given
		given(userRepository.findByUsername(loginRequest.getUsername())).willReturn(mock(User.class));

		// when
		userService.checkUsername(loginRequest.getUsername());

		// then
		verify(userRepository, times(1)).findByUsername(loginRequest.getUsername());
	}

	/**
	 * 로그인 시 사용자 전달한 계정과 일치하는 계정이 없는 경우
	 * 작성자 : 김은정
	 */
	@Test
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

	/**
	 * 회원가입 시 이메일이 이미 존재하는 경우를 테스트합니다.
	 * 작성자: 배서진
	 *
	 * @given 이메일이 이미 존재하는 경우
	 * @when joinUser 메서드가 호출될 때
	 * @then DuplicateUserInfoException 예외가 발생해야 하며, UserRepository의 save 메서드는 호출되지 않아야 합니다.
	 */
	@Test
	public void testJoinUserEmailAlreadyExists() {
		// given
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

		// when & then
		assertThrows(DuplicateUserInfoException.class, () -> userService.joinUser(request));

		verify(userRepository, never()).save(Mockito.any(User.class));
	}

	/**
	 * 회원가입 시 사용자명이 이미 존재하는 경우를 테스트합니다.
	 * 작성자: 배서진
	 *
	 * @given 사용자명이 이미 존재하는 경우
	 * @when joinUser 메서드가 호출될 때
	 * @then DuplicateUserInfoException 예외가 발생해야 하며, UserRepository의 save 메서드는 호출되지 않아야 합니다.
	 */
	@Test
	public void testJoinUserUsernameAlreadyExists() {
		// given
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

		// when & then
		assertThrows(DuplicateUserInfoException.class, () -> userService.joinUser(request));

		verify(userRepository, never()).save(Mockito.any(User.class));
	}

	/**
	 * 회원가입 시 비밀번호 길이가 유효하지 않은 경우를 테스트합니다.
	 * 작성자: 배서진
	 *
	 * @given 비밀번호 길이가 10자 미만이거나 20자를 초과하는 경우
	 * @when joinUser 메서드가 호출될 때
	 * @then CustomException 예외가 발생해야 하며, UserRepository의 save 메서드는 호출되지 않아야 합니다.
	 */
	@Test
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

	/**
	 * 회원가입 시 비밀번호에 숫자, 문자, 특수문자 중 두 가지 이상이 포함되지 않은 경우를 테스트합니다.
	 * 작성자: 배서진
	 *
	 * @given 비밀번호가 숫자, 문자, 특수문자 중 두 가지 이상을 포함하지 않은 경우
	 * @when joinUser 메서드가 호출될 때
	 * @then CustomException 예외가 발생해야 하며, UserRepository의 save 메서드는 호출되지 않아야 합니다.
	 */
	@Test
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

	/**
	 * 회원가입 시 사용하고자 하는 계정을 이미 다른 사용자가 사용한 경우를 테스트합니다.
	 * 작성자 : 김은정
	 */
	@Test
	public void duplicateAccountExistsTest() {
		//given
		String username = "username";
		User user = User.builder()
				.username(username)
				.email("welfjlkd@gmail.com")
				.password("padlfjdl")
				.status(UserStatus.ACTIVE)
				.certifyStatus(UserCertifyStatus.AUTHENTICATED)
				.build();
		given(userRepository.findByUsername(any())).willReturn(user);


		//when
		String result = userService.checkDuplicateAccount(username);

		//then
		assertThat(result).isEqualTo(username);
		verify(userRepository, times(1)).findByUsername(username);
	}

	/**
	 * 회원가입 시 사용하고자 하는 계정을 다른 사용자가 사용하지 않는 경우를 테스트합니다.
	 * 작성자 : 김은정
	 */
	@Test
	public void duplicateAccountNoExistsTest() {
		//when
		String result = userService.checkDuplicateAccount(any());

		//then
		assertThat(result).isEqualTo("해당 아이디는 사용이 가능합니다.");
		verify(userRepository, times(1)).findByUsername(any());
	}
}
