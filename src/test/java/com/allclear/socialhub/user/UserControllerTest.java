package com.allclear.socialhub.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.allclear.socialhub.common.provider.JwtTokenProvider;
import com.allclear.socialhub.user.dto.UserJoinRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private UserJoinRequest userJoinRequest;

	@BeforeEach
	public void setUp() {
		userJoinRequest = new UserJoinRequest("username", "popcorn23@gmail.com", "qlalfqjsghek23");

		MultiValueMap<String, Object> joinMap = new LinkedMultiValueMap<>();
		joinMap.add("username", userJoinRequest.getUsername());
		joinMap.add("email", userJoinRequest.getEmail());
		joinMap.add("password", userJoinRequest.getPassword());

		testRestTemplate.postForObject("/api/users/", joinMap, String.class);
	}

	@Test
	public void 사용자_로그인_테스트() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("username", userJoinRequest.getUsername());
		map.add("password", userJoinRequest.getPassword());

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
				httpEntity, String.class);

		String jwtToken = responseEntity.getHeaders().getFirst("AUTHORIZATION");
		jwtTokenProvider.extractAllClaims(jwtToken);
	}
}
