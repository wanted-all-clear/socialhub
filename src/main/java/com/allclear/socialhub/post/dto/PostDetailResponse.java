package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.PostType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
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

    // QueryDsl 에서 사용되는 Builder
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
