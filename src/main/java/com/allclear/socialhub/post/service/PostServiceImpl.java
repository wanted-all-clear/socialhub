package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;
import com.allclear.socialhub.post.common.hashtag.repository.PostHashtagRepository;
import com.allclear.socialhub.post.common.hashtag.service.HashtagService;
import com.allclear.socialhub.post.common.like.domain.PostLike;
import com.allclear.socialhub.post.common.like.dto.PostLikeResponse;
import com.allclear.socialhub.post.common.like.repository.PostLikeRepository;
import com.allclear.socialhub.post.common.share.domain.PostShare;
import com.allclear.socialhub.post.common.share.dto.PostShareResponse;
import com.allclear.socialhub.post.common.share.repository.PostShareRepository;
import com.allclear.socialhub.post.common.view.repository.PostViewRepository;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.*;
import com.allclear.socialhub.post.repository.PostRepository;
import com.allclear.socialhub.user.domain.User;
import com.allclear.socialhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.allclear.socialhub.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostShareRepository postShareRepository;
    private final PostViewRepository postViewRepository;

    /**
     * 1. 게시물 등록
     * 작성자 : 오예령
     *
     * @param createRequest 게시물 타입, 제목, 내용, 해시태그리스트
     * @return 생성된 게시물 PostResponse에 담아 반환
     */
    @Override
    public PostResponse createPost(String username, PostCreateRequest createRequest) {

        // 0. 유저 검증
        User user = userCheck(username);

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
     * 2. 게시물 수정
     * 작성자 : 오예령
     *
     * @param username      유저 계정명
     * @param postId        게시물Id
     * @param updateRequest 게시물 제목, 내용, 해시태그리스트
     * @return 수정된 게시물 PostResponse에 담아 반환
     */
    @Override
    @Transactional
    public PostResponse updatePost(String username, Long postId, PostUpdateRequest updateRequest) {

        // 0. 유저 검증
        userCheck(username);

        // 1. 게시물 검증
        Post post = postCheck(postId);
        if (!post.getUser().getUsername().equals(username)) throw new CustomException(POST_OWNER_MISMATCH);

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
     * 3. 게시물 삭제
     * 작성자 : 오예령
     *
     * @param username 유저 계정명
     * @param postId   게시물Id
     */

    @Override
    @Transactional
    public void deletePost(String username, Long postId) {

        userCheck(username);
        Post post = postCheck(postId);
        if (!post.getUser().getUsername().equals(username)) throw new CustomException(POST_OWNER_MISMATCH);

        // 해시태그 연관관계 삭제
        hashtagService.deleteByPostId(postId);
        // 게시물 좋아요 삭제
        postLikeRepository.deleteAllByPostId(postId);
        // 게시물 공유 삭제
        postShareRepository.deleteAllByPostId(postId);
        // 게시물 조회수 삭제
        postViewRepository.deleteAllByPostId(postId);
        // 게시물 삭제
        postRepository.delete(post);

    }

    /* 4. 게시물 검색 목록 조회
     * 작성자 : 오예령
     *
     * @param pageable Pagination 요청 정보 관련 인터페이스
     * @param username 유저 계정이름
     * @param hashtag  검색할 hashtag
     * @param type     게시물 타입 ("INSTAGRAM", "FACEBOOK", "TWITTER", "THREADS" 만 가능)
     * @param query    검색할 query
     * @param orderBy  정렬기준
     * @param sort     순서
     * @param searchBy 검색 범위 ("TITLE", "CONTENT", "TITLE, CONTENT"만 가능)
     * @return 페이징 처리가 된 검색 결과에 맞는 목록 반환
     */
    @Override
    public PostPaging searchPosts(Pageable pageable, String username, String hashtag, PostType type, String query, String orderBy, String sort, String searchBy) {

        return new PostPaging(postRepository.searchPosts(pageable, username, hashtag, type, query, orderBy, sort, searchBy));
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
     * 6. 게시물 상세 조회
     * 작성자 : 유리빛나
     *
     * @param postId   게시물 번호
     * @param username 유저 계정명
     * @return 게시물 상세
     */
    public PostDetailResponse getPostDetail(Long postId, String username) {

        postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return postRepository.getPostDetail(postId, username);
    }

    /**
     * 7. 게시물 좋아요
     * 작성자 : 유리빛나
     *
     * @param postId   게시물 번호
     * @param username 유저 계정명
     * @return 게시물 ID, 게시물 좋아요 수, 외부 API URL이 포함된 PostLikeResponse 객체
     */
    public PostLikeResponse likePost(Long postId, String username) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        PostLike postLike = PostLike.builder()
                .user(userRepository.getReferenceById(userCheck(username).getId()))
                .post(post)
                .build();

        // 게시물 좋아요 데이터 생성
        postLikeRepository.save(postLike);

        // 게시물의 좋아요수 증가
        post.updateLikeCnt(post);
        postRepository.save(post);

        String url = sendToSnsApi(String.valueOf(post.getType()), "likes");

        return PostLikeResponse.builder()
                .postId(postId)
                .likeCnt(postLike.getPost().getLikeCnt())
                .url(url)
                .build();
    }

    /**
     * 8. 게시물 공유
     * 작성자 : 유리빛나
     *
     * @param postId   게시물 번호
     * @param username 유저 계정명
     */
    public PostShareResponse sharePost(Long postId, String username) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        PostShare postShare = PostShare.builder()
                .user(userRepository.getReferenceById(userCheck(username).getId()))
                .post(post)
                .build();

        // 게시물 공유 데이터 생성
        postShareRepository.save(postShare);

        // 게시물의 공유수 증가
        post.updateShareCnt(post);
        postRepository.save(post);

        String url = sendToSnsApi(String.valueOf(post.getType()), "share");

        return PostShareResponse.builder()
                .postId(postId)
                .shareCnt(postShare.getPost().getShareCnt())
                .url(url)
                .build();
    }

    /**
     * 회원 검증
     * 작성자 : 오예령
     *
     * @param username 유저 계정명
     * @return 해당 회원을 반환
     */
    private User userCheck(String username) {

        try {
            return userRepository.findByUsername(username);
        } catch (NullPointerException e) {
            throw new CustomException(USER_NOT_EXIST);
        }
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
     * 게시물 좋아요, 공유 관련 외부 API 호출
     * 작성자 : 유리빛나
     *
     * @param postType SNS 타입
     * @param apiType  좋아요 or 공유 타입
     * @return 외부 API URL
     */
    private String sendToSnsApi(String postType, String apiType) {

        // 1. Rest 방식의 API를 호출할 수 있는 Spring 내장 클래스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 2. Enum 타입이었던 PostType 문자열을 소문자로 변환
        String lowerCasePostType = postType.toLowerCase();

        // 3. 호출할 외부 API
        String url = "https://www." + lowerCasePostType + ".com/" + apiType + "/" + lowerCasePostType;

        Map<String, Object> request = new HashMap<>();
        request.put("contentId", lowerCasePostType);

        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            log.info("외부 API 호출에 실패하였습니다.");
        }

        return url;
    }

}
