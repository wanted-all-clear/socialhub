package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.common.share.dto.PostShareResponse;
import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.dto.PostResponse;
import com.allclear.socialhub.post.service.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false) // Spring Security 제외 (추후 유저 검증 로직 구현 후 제거 예정)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostServiceImpl postService;

    @DisplayName("게시물 목록을 조회합니다.")
    @Test
    void getPosts() throws Exception {
        // given
        List<PostResponse> postList = List.of(
                new PostResponse(1L, 1L, "망원동 맛집", "맛집 소개해요",
                        List.of("#망원동", "#맛집"), 10, 10, 10,
                        LocalDateTime.of(2024, 8, 25, 12, 0, 0),
                        LocalDateTime.of(2024, 8, 25, 12, 0, 0)),
                new PostResponse(2L, 1L, "영화 추천", "영화 추천합니다",
                        List.of("#영화", "#해리포터"), 20, 20, 20,
                        LocalDateTime.of(2024, 8, 25, 12, 0, 0),
                        LocalDateTime.of(2024, 8, 25, 12, 0, 0))
        );

        PostPaging postPaging = new PostPaging(2, postList, 10, 0, 1);
        ResponseEntity<PostPaging> result = new ResponseEntity<>(postPaging, HttpStatus.OK);

        when(postService.getPosts(any(Pageable.class))).thenReturn(result.getBody());

        // when // then
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("sort", "postId,DESC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postCnt").value(2))
                .andExpect(jsonPath("$.postList[0].postId").value(1))
                .andExpect(jsonPath("$.postList[0].title").value("망원동 맛집"))
                .andExpect(jsonPath("$.postList[0].content").value("맛집 소개해요"))
                .andExpect(jsonPath("$.postList[1].postId").value(2))
                .andExpect(jsonPath("$.postList[1].title").value("영화 추천"))
                .andExpect(jsonPath("$.postList[1].content").value("영화 추천합니다"));

        verify(postService).getPosts(any(Pageable.class));
    }

    @DisplayName("게시물 공유를 추가합니다.")
    @Test
    void sharePost() throws Exception {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String url = "https://www.facebook.com/share/facebook";

        PostShareResponse response = PostShareResponse.builder()
                .postId(postId)
                .shareCnt(10)
                .url(url)
                .build();

        when(postService.sharePost(postId, userId)).thenReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/posts/share/{postId}", postId)
                                .param("userId", userId.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.shareCnt").value(10))
                .andExpect(jsonPath("$.url").value(url));
    }

}
