package com.allclear.socialhub.post.repository.querydsl;

import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.dto.PostDetailResponse;
import com.allclear.socialhub.post.dto.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuerydsl {

    Page<PostListResponse> getPosts(Pageable pageable);

    PostDetailResponse getPostDetail(Long postId, String username);

    Page<PostListResponse> searchPosts(Pageable pageable, String username, String hashtagQuery, PostType type, String query, String orderBy, String sort, String searchBy);

}
