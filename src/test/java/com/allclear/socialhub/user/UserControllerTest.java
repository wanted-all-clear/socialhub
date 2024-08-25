package com.allclear.socialhub.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.dto.UserLoginRequest;
import com.allclear.socialhub.user.type.UsernameDupStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

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
}
