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
import com.allclear.socialhub.post.repository.PostRepository;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.allclear.socialhub.common.exception.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;

    /**
     * 1. 게시물 등록
     * 작성자 : 오예령
     *
     * @param createRequest
     * @return 생성된 게시물 PostResponse에 담아 반환
     */
    @Override
    public PostResponse createPost(Long userId, PostCreateRequest createRequest) {
        // 1. 유저 검증
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_EXIST)
        );
        // 2. 게시물 등록
        Post post = postRepository.save(createRequest.toEntity(user));

        // 3. 해시태그 등록
        List<Hashtag> savedHashtags = hashtagService.toEachHashtag(createRequest.getHashtagList());

        for (Hashtag hashtag : savedHashtags) {
            PostHashtag postHashtag = PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();
            postHashtagRepository.save(postHashtag);
        }
        return PostResponse.fromEntity(post, createRequest.getHashtagList());
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

}
