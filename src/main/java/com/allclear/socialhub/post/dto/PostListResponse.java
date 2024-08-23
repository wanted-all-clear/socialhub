package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.PostType;
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
public class PostListResponse {

    private Long postId;
    private List<String> hashtagList;
    private PostType type;
    private String title;
    private String content;
    private int viewCnt;
    private int likeCnt;
    private int shareCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
