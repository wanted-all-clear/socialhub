package com.allclear.socialhub.post.common.like.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {

    private Long postId;
    private int likeCount;
    String url;

}
