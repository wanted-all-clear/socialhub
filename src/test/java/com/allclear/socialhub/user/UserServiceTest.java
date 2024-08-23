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

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.dto.UserLoginDto;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository repo;

	@InjectMocks
	private UserServiceImpl service;

	private UserLoginDto loginDto;

	@BeforeEach
	public void setUp() {
		loginDto = mock(UserLoginDto.class);
	}

	@Test
	public void 사용자_로그인_테스트() {
		service.userLogin(loginDto);

		verify(service, times(1)).checkUsername(loginDto.getUsername());
	}

	@Test
	public void 사용자_아이디_조회_성공_테스트() {
		given(repo.findByUsername(loginDto.getUsername())).willReturn(mock(User.class));
		User result = service.checkUsername(loginDto.getUsername());

		verify(repo, times(1)).findByUsername(loginDto.getUsername());
	}

	@Test
	public void 사용자_아이디_조회_실패_테스트() {
		Throwable throwable = assertThrows(RuntimeException.class, () -> service.checkUsername(loginDto.getUsername()));

		verify(repo, times(1)).findByUsername(loginDto.getUsername());
		assertThat(throwable.getMessage()).isEqualTo(ErrorCode.USER_NOT_EXIST.getMessage());
	}
}
