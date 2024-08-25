package com.allclear.socialhub.post.common.share.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostShareResponse {

    private Long postId;
    private int shareCount;
    String url;

}
