package com.allclear.socialhub.post.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;

@Service
public interface PostService {

	PostResponse createPost(Long userId, PostCreateRequest requestDto);

	PostPaging getPosts(Pageable pageable);

}
