package com.allclear.socialhub.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.allclear.socialhub.post.domain.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

	private Long postId;
	private Long userId;
	private String title;
	private String content;
	private List<String> hashtagList;
	private int viewCnt;
	private int likeCnt;
	private int shareCnt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static PostResponse fromEntity(Post post, List<String> hashTagList) {

		return PostResponse.builder()
				.postId(post.getId())
				.userId(post.getUser().getId())
				.title(post.getTitle())
				.content(post.getContent())
				.hashtagList(hashTagList)
				.viewCnt(post.getViewCnt())
				.likeCnt(post.getLikeCnt())
				.shareCnt(post.getShareCnt())
				.createdAt(post.getCreatedAt())
				.updatedAt(post.getUpdatedAt())
				.build();
	}

}
