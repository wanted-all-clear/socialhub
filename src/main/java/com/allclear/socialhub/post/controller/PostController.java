package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public PostPaging getPosts(@PageableDefault Pageable pageable) {

        return postService.getPosts(pageable);

    }

}
