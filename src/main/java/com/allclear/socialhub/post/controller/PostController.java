package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.common.like.dto.PostLikeResponse;
import com.allclear.socialhub.post.common.share.dto.PostShareResponse;
import com.allclear.socialhub.post.dto.PostCreateRequest;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.dto.PostUpdateRequest;
import com.allclear.socialhub.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "게시물 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 등록", description = "게시물을 등록합니다.")
    @PostMapping
    public ResponseEntity<PostResponse> creatPost(@Valid @RequestBody PostCreateRequest requestDto) {

        // TODO : 유저 받아오는 형식 추후 변경 예정
        return ResponseEntity.status(201).body(postService.createPost(1L, requestDto));
    }

    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<PostPaging> getPosts(@PageableDefault Pageable pageable) {

        // TODO : 추후 유저 검증 필요
        return ResponseEntity.status(200).body(postService.getPosts(pageable));

    }

    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostUpdateRequest updateRequest,
                                                   @PathVariable Long postId) {

        return ResponseEntity.status(200).body(postService.updatePost(1L, postId, updateRequest));
    }

    @Operation(summary = "게시물 좋아요", description = "게시물 좋아요를 추가합니다.")
    @PostMapping("/like/{postId}")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long postId, @RequestParam Long userId) {

        return ResponseEntity.status(201).body(postService.likePost(postId, userId));
    }

    @Operation(summary = "게시물 공유", description = "게시물 공유를 추가합니다.")
    @PostMapping("/share/{postId}")
    public ResponseEntity<PostShareResponse> sharePost(@PathVariable Long postId, @RequestParam Long userId) {

        // TODO: 추후 유저 검증
        return ResponseEntity.status(201).body(postService.sharePost(postId, userId));
    }

}
