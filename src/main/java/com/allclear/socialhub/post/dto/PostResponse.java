package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private Long userId;
    private PostType type;
    private String title;
    private String content;
    private List<String> hashtagList;
    private int viewCnt;
    private int likeCnt;
    private int shareCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse fromEntity(Post post, List<String> hashtagList) {

        return PostResponse.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .type(post.getType())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagList(hashtagList)
                .viewCnt(post.getViewCnt())
                .likeCnt(post.getLikeCnt())
                .shareCnt(post.getShareCnt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
