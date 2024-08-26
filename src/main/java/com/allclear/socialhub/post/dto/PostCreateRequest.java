package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @NotNull(message = "게시물 타입을 확인해주세요.")
    private PostType type;
    @NotNull(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String content;
    private List<String> hashtagList;

    public Post toEntity(User user) {

        return Post.builder()
                .user(user)
                .type(type)
                .title(title)
                .content(content)
                .build();
    }

}
