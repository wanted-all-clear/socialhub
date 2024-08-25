package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;
import com.allclear.socialhub.post.common.hashtag.repository.PostHashtagRepository;
import com.allclear.socialhub.post.common.hashtag.service.HashtagService;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.dto.PostUpdateRequest;
import com.allclear.socialhub.post.repository.PostRepository;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.allclear.socialhub.common.exception.ErrorCode.POST_NOT_FOUND;
import static com.allclear.socialhub.common.exception.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;

    /**
     * 0. 게시물 등록
     * 작성자 : 오예령
     *
     * @param createRequest 게시물 타입, 제목, 내용, 해시태그리스트
     * @return 생성된 게시물 PostResponse에 담아 반환
     */
    @Override
    public PostResponse createPost(Long userId, PostCreateRequest createRequest) {

        // 0. 유저 검증
        User user = userCheck(userId);

        // 1. 게시물 등록
        Post post = postRepository.save(createRequest.toEntity(user));

        // 2. 해시태그 등록
        List<String> cleanedHashtagList = hashtagService.removeHashSymbol(createRequest.getHashtagList());
        List<Hashtag> savedHashtags = hashtagService.createHashtag(cleanedHashtagList);

        // 3. 연관관계 등록
        savePostHashtag(post, savedHashtags);

        return PostResponse.fromEntity(post, createRequest.getHashtagList());
    }

    /**
     * 1. 게시물 수정
     * 작성자 : 오예령
     *
     * @param userId        유저Id
     * @param postId        게시물Id
     * @param updateRequest 게시물 제목, 내용, 해시태그리스트
     * @return 수정된 게시물 PostResponse에 담아 반환
     */
    @Override
    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostUpdateRequest updateRequest) {

        // 0. 유저 검증
        userCheck(userId);

        // 1. 게시물 검증
        Post post = postCheck(postId);

        Post updatePost = updateRequest.toEntity();
        post.update(updatePost);

        // 2. 해시태그 수정
        List<Hashtag> savedHashtags = hashtagService.updateHashtag(postId, updateRequest.getHashtagList());

        // 3. 연관관계 수정
        savePostHashtag(post, savedHashtags);

        // 4. 수정된 hashtagList 반환
        List<String> updatedHashtagList = new ArrayList<>();

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPostId(postId);

        for (PostHashtag postHashtag : postHashtags) {
            updatedHashtagList.add("#" + postHashtag.getHashtag().getContent());
        }

        return PostResponse.fromEntity(post, updatedHashtagList);
    }

    /**
     * PostHashtag 연관관계 등록
     * 작성자 : 오예령
     *
     * @param post     게시물
     * @param hashtags 해시태그
     */
    private void savePostHashtag(Post post, List<Hashtag> hashtags) {

        for (Hashtag hashtag : hashtags) {
            PostHashtag postHashtag = PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();
            postHashtagRepository.save(postHashtag);
        }
    }


    /**
     * 5. 게시물 목록 조회
     * 작성자 : 유리빛나
     *
     * @param pageable Pagination 요청 정보 관련 인터페이스
     * @return 페이징 처리가 된 게시물 전체 목록
     */
    public PostPaging getPosts(Pageable pageable) {

        return new PostPaging(postRepository.getPosts(pageable));
    }

    /**
     * 회원 검증
     * 작성자 : 오예령
     *
     * @param userId 유저Id
     * @return 해당 회원을 반환
     */
    private User userCheck(Long userId) {

        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_EXIST)
        );
    }

    /**
     * 게시물 검증
     * 작성자 : 오예령
     *
     * @param postId 게시물Id
     * @return 해당 게시물을 반환
     */
    private Post postCheck(Long postId) {

        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
    }

}
