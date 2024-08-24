package com.allclear.socialhub.post.repository.querydsl;

import com.allclear.socialhub.post.dto.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuerydsl {

    Page<PostListResponse> getPosts(Pageable pageable);

}
