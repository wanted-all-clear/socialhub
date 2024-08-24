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
    private final HashtagService hashTagService;
    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;

    /**
     * 1. 게시물 등록
     * 작성자 : 오예령
     *
     * @param requestPostDto
     * @return
     */
    @Override
    public PostResponse createPost(Long userId, PostCreateRequest requestPostDto) {
        // 1. 유저 검증
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_EXIST)
        );
        // 2. 게시물 등록
        Post post = postRepository.save(requestPostDto.toEntity(user));

        // 3. 해시태그 등록
        List<Hashtag> savedHashtags = hashTagService.toEachHashTag(requestPostDto.getHashtagList());

        for (Hashtag hashTag : savedHashtags) {
            PostHashtag postHashTag = PostHashtag.builder()
                    .post(post)
                    .hashTag(hashTag)
                    .build();
            postHashtagRepository.save(postHashTag);
        }
        return PostResponse.fromEntity(post, requestPostDto.getHashtagList());
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
