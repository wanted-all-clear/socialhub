package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<PostResponse> creatPost(@RequestBody PostCreateRequest requestDto) {

        // TODO : 유저 받아오는 형식 추후 변경 예정
        return ResponseEntity.status(201).body(postService.createPost(1L, requestDto));
    }

    @GetMapping
    public ResponseEntity<PostPaging> getPosts(@PageableDefault Pageable pageable) {

        return ResponseEntity.status(200).body(postService.getPosts(pageable));

    }

}
