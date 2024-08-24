package com.allclear.socialhub.post.dto;

import java.util.List;

import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

	private PostType type;
	private String title;
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
