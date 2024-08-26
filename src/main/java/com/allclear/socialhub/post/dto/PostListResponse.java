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
public class PostListResponse {

    private Long postId;
    private List<String> hashtagList;
    private PostType type;
    private String title;
    private String content;
    private int viewCnt;
    private int likeCnt;
    private int shareCnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public void setContent(String content) {

        if (content != null && content.length() > 20) {
            this.content = content.substring(0, 20); // 글자수 제한 적용
        } else {
            this.content = content;
        }
    }

}
