package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;
import com.allclear.socialhub.post.common.hashtag.repository.HashtagRepository;
import com.allclear.socialhub.post.common.hashtag.repository.PostHashtagRepository;
import com.allclear.socialhub.post.common.like.repository.PostLikeRepository;
import com.allclear.socialhub.post.common.share.repository.PostShareRepository;
import com.allclear.socialhub.post.common.view.repository.PostViewRepository;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.dto.PostUpdateRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static com.allclear.socialhub.common.exception.ErrorCode.USER_NOT_EXIST;
import static com.allclear.socialhub.post.domain.PostType.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

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
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostShareRepository postShareRepository;
    @Autowired
    private PostViewRepository postViewRepository;

    static
    List<String> hashtagList = Arrays.asList("#테스트", "#자바", "#스프링");

    @AfterEach
    void tearDown() {

        postHashtagRepository.deleteAllInBatch();
        hashtagRepository.deleteAllInBatch();
        postLikeRepository.deleteAllInBatch();
        postShareRepository.deleteAllInBatch();
        postViewRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

    }

    @Test
    @DisplayName("게시물을 등록합니다.")
    void createPost() {
        // given
        User user = createUser();

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

    @Test
    @DisplayName("존재하지 않는 유저Id로 게시물을 등록합니다.")
    void createPostWithNonExistUser() {
        // given
        Long nonExistUserId = -1L;  // 실제로 존재하지 않는 ID

        PostCreateRequest request = PostCreateRequest.builder()
                .type(INSTAGRAM)
                .title("테스트제목")
                .content("테스트내용")
                .hashtagList(hashtagList)
                .build();

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> postService.createPost(nonExistUserId, request));

        assertEquals(USER_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 게시물 타입으로 게시물을 등록합니다.")
    void createPostWithNonExistType() {
        // given
        User user = createUser();

        PostCreateRequest request = PostCreateRequest.builder()
                .type(null)
                .title("테스트제목")
                .content("테스트내용")
                .hashtagList(hashtagList)
                .build();

        // when & then
        assertThatThrownBy(() -> postService.createPost(user.getId(), request)).isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("게시물 등록 시 제목은 필수 입력값입니다.")
    void createPostWithoutTitle() {
        // given
        User user = createUser();

        PostCreateRequest request = PostCreateRequest.builder()
                .type(FACEBOOK)
                .title(null)
                .content("테스트내용")
                .hashtagList(hashtagList)
                .build();

        // when & then
        assertThatThrownBy(() -> postService.createPost(user.getId(), request)).isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("게시물 등록 시 내용은 필수 입력값입니다.")
    void createPostWithoutContent() {
        // given
        User user = createUser();

        PostCreateRequest request = PostCreateRequest.builder()
                .type(FACEBOOK)
                .title("테스트제목")
                .content(null)
                .hashtagList(hashtagList)
                .build();

        // when & then
        assertThatThrownBy(() -> postService.createPost(user.getId(), request)).isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("게시물을 수정합니다.")
    void updatePost() {
        // given
        User user = createUser();
        userRepository.save(user);

        Post post = createPost(user, "테스트제목", "테스트내용", INSTAGRAM, 0, 0, 0);
        postRepository.save(post);

        Hashtag hashtag = createHashtag("#해시태그");
        hashtagRepository.save(hashtag);

        PostHashtag postHashtag = createPostHashtag(post, hashtag);
        postHashtagRepository.save(postHashtag);

        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("테스트제목수정")
                .content("테스트내용수정")
                .hashtagList(hashtagList)
                .build();

        // when
        PostResponse response = postService.updatePost(user.getId(), post.getId(), updateRequest);

        // then
        assertNotNull(response);
        assertEquals("테스트제목수정", response.getTitle());
        assertEquals("테스트내용수정", response.getContent());
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

    // 해시태그 빌더 생성
    private Hashtag createHashtag(String content) {

        return Hashtag.builder()
                .content(content)
                .build();
    }

    // 게시물 해시태그 빌더 생성
    private PostHashtag createPostHashtag(Post post, Hashtag hashtag) {

        return PostHashtag.builder()
                .post(post)
                .hashtag(hashtag)
                .build();
    }

}
