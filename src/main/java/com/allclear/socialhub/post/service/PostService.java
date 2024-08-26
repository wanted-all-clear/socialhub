package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.common.like.dto.PostLikeResponse;
import com.allclear.socialhub.post.common.share.dto.PostShareResponse;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    PostResponse createPost(Long userId, PostCreateRequest requestDto);

    PostResponse updatePost(Long userId, Long postId, PostUpdateRequest updateRequest);

    void deletePost(Long userId, Long postId);

    PostPaging searchPosts(Pageable pageable, String username, String hashtag, PostType type, String query, String orderBy, String sort, String searchBy);

    PostPaging getPosts(Pageable pageable);

    PostLikeResponse likePost(Long postId, Long userId);

    PostShareResponse sharePost(Long postId, Long userId);

    PostDetailResponse getPostDetail(Long postId, Long userId);

}
