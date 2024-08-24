package com.allclear.socialhub.post.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;

@SpringBootTest
class PostServiceImplTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostServiceImpl postService;

	@BeforeEach
	@Transactional
	public void beforeEach() {

		User user = User.builder()
				.id(1L)
				.username("testUsername")
				.email("testEmail@test.com")
				.password("testPassword@")
				.status(UserStatus.ACTIVE)
				.certifyStatus(UserCertifyStatus.AUTHENTICATED)
				.build();
		userRepository.save(user);

	}

	@Test
	@DisplayName("게시물을 등록합니다.")
	void createPost() {
		// given
		User user = userRepository.findById(1L).orElseThrow(
				() -> new CustomException(ErrorCode.USER_NOT_EXIST)
		);

		List<String> hashtagList = Arrays.asList("#테스트", "#자바", "#스프링");

		PostCreateRequest request = PostCreateRequest.builder()
				.type(PostType.INSTAGRAM)
				.title("테스트제목")
				.content("테스트내용")
				.hashtagList(hashtagList)
				.build();

		// when
		PostResponse response = postService.createPost(user.getId(), request);

		// then
		assertNotNull(response);
		assertEquals("테스트제목", response.getTitle());
		assertEquals("테스트내용", response.getContent());
		assertEquals(3, response.getHashtagList().size());
	}

}
