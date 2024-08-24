package com.allclear.socialhub.post.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<PostResponse> creatPost(@RequestBody PostCreateRequest requestDto) {

		return ResponseEntity.status(201).body(postService.createPost(1L, requestDto));
	}

	@GetMapping
	public PostPaging getPosts(@PageableDefault Pageable pageable) {

		return postService.getPosts(pageable);

	}

}
