package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.dto.PostUpdateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    PostResponse createPost(Long userId, PostCreateRequest requestDto);

    PostResponse updatePost(Long userId, Long postId, PostUpdateRequest updateRequest);

    PostPaging getPosts(Pageable pageable);

}
