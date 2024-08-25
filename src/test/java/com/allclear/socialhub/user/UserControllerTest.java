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

import io.jsonwebtoken.Claims;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * 로그인 통합 테스트
	 */
	@Test
	public void 사용자_로그인_테스트() {
		UserLoginRequest login = new UserLoginRequest("user1", "abcd1234..");

		HttpEntity<UserLoginRequest> httpEntity = new HttpEntity<>(login);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
				httpEntity, String.class);

		String jwtToken = responseEntity.getHeaders().getFirst("AUTHORIZATION");
		Claims claims = jwtTokenProvider.extractAllClaims(jwtToken);
		String payload = claims.getSubject();
		String[] result = payload.split(",");
		String resultUsername = result[0].trim();
		String resultEmail = result[1].trim();

		assertThat(resultUsername).isEqualTo("user1");
		assertThat(resultEmail).isEqualTo("user1@test.com");
	}
}
