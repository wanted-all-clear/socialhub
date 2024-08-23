package com.allclear.socialhub.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allclear.socialhub.common.config.WebSecurityConfig;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserLoginDto;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository repo;

	@Mock
	private WebSecurityConfig config;

	@InjectMocks
	private UserServiceImpl service;

	private UserLoginDto loginDto;

	@BeforeEach
	public void setUp() {
		loginDto = mock(UserLoginDto.class);
	}

	@Test
	public void userLoginTest() {
		service.userLogin(loginDto);

		verify(service, times(1)).checkUsername(loginDto.getUsername());
	}

	@Test
	public void checkUsernameSuccessTest() {
		given(repo.findByUsername(loginDto.getUsername())).willReturn(mock(User.class));
		service.checkUsername(loginDto.getUsername());

		verify(repo, times(1)).findByUsername(loginDto.getUsername());
	}

	@Test
	public void checkUsernameFailTest() {
		Throwable throwable = assertThrows(RuntimeException.class, () -> service.checkUsername(loginDto.getUsername()));

		verify(repo, times(1)).findByUsername(loginDto.getUsername());
		assertThat(throwable.getMessage()).isEqualTo(ErrorCode.USER_NOT_EXIST.getMessage());
	}
}
