package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateRequest {

    private String title;
    private String content;
    private List<String> hashtagList;

    public Post toEntity() {

        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
