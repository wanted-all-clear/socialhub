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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void 사용자_로그인_테스트() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("username", "username");
		map.add("password", "password");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange("/api/users/login", HttpMethod.POST,
				httpEntity, String.class);

		responseEntity.getHeaders();
	}
}
