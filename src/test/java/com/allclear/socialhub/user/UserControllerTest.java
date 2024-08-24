package com.allclear.socialhub.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
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

	private String username;
	private String password;
	private String email;

	@BeforeEach
	public void setUp() {
		username = "user1";
		password = "abcd1234..";
		email = "user1@test.com";
	}

	@Test
	public void 사용자_로그인_테스트() {
		UserLoginRequest login = new UserLoginRequest(username, password);

		HttpEntity<UserLoginRequest> httpEntity = new HttpEntity<>(login);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
				httpEntity, String.class);

		String jwtToken = responseEntity.getHeaders().getFirst("AUTHORIZATION");
		Claims claims = jwtTokenProvider.extractAllClaims(jwtToken);
		String payload = claims.getSubject();
		String[] result = payload.split(",");
		String resultUsername = result[0].trim();
		String resultEmail = result[1].trim();

		assertThat(resultUsername).isEqualTo(username);
		assertThat(resultEmail).isEqualTo(email);
	}
}
