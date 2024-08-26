package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostUpdateRequest {

    @NotNull(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String content;
    private List<String> hashtagList;

    public Post toEntity() {

        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
