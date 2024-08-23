package com.allclear.socialhub.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void 사용자_로그인_테스트() {

	}
}
