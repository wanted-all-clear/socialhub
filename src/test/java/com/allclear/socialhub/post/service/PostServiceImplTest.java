package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.common.hashtag.repository.HashtagRepository;
import com.allclear.socialhub.post.common.hashtag.repository.PostHashtagRepository;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.repository.PostRepository;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static com.allclear.socialhub.post.domain.PostType.*;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {

        postHashtagRepository.deleteAllInBatch();
        hashtagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

    }

    @Test
    @DisplayName("게시물을 등록합니다.")
    void createPost() {
        // given
        User user = createUser();

        user = userRepository.findById(1L).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_EXIST)
        );

        List<String> hashtagList = Arrays.asList("#테스트", "#자바", "#스프링");

        PostCreateRequest request = PostCreateRequest.builder()
                .type(INSTAGRAM)
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

    @DisplayName("게시물 목록을 조회합니다.")
    @Test
    void getPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("id")));

        User user = createUser();

        Post post1 = createPost(user, "제목1", "내용1", INSTAGRAM, 10, 10, 10);
        Post post2 = createPost(user, "제목2", "내용2", FACEBOOK, 20, 20, 20);
        Post post3 = createPost(user, "제목3", "내용3", TWITTER, 30, 30, 30);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when // then
        Assertions.assertThat(postService.getPosts(pageable).getPostList()).hasSize(3)
                .extracting("title", "content", "type", "likeCnt", "shareCnt", "viewCnt")
                .containsExactlyInAnyOrder(
                        tuple("제목3", "내용3", TWITTER, 30, 30, 30),
                        tuple("제목2", "내용2", FACEBOOK, 20, 20, 20),
                        tuple("제목1", "내용1", INSTAGRAM, 10, 10, 10)
                );
    }

    // 유저 빌더 생성
    private User createUser() {

        User user = User.builder()
                .username("testUsername")
                .email("testEmail@test.com")
                .password("testPassword@")
                .status(UserStatus.ACTIVE)
                .certifyStatus(UserCertifyStatus.AUTHENTICATED)
                .build();

        return userRepository.save(user);
    }

    // 게시물 빌더 생성
    private Post createPost(User user, String title, String content, PostType type,
                            int viewCnt, int likeCnt, int shareCnt) {

        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .viewCnt(viewCnt)
                .likeCnt(likeCnt)
                .shareCnt(shareCnt)
                .build();
    }

}
