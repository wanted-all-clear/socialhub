package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.PostType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private Long userId;
    private PostType type;
    private String title;
    private String content;
    private List<String> hashtagList;
    private int viewCnt;
    private int likeCnt;
    private int shareCnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public PostDetailResponse(Long postId, Long userId, PostType type, String title, String content, int viewCnt, int likeCnt, int shareCnt, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.postId = postId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.shareCnt = shareCnt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
