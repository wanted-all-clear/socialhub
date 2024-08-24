package com.allclear.socialhub.post.repository.querydsl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.allclear.socialhub.post.common.hashtag.domain.QHashtag;
import com.allclear.socialhub.post.common.hashtag.domain.QPostHashtag;
import com.allclear.socialhub.post.domain.QPost;
import com.allclear.socialhub.post.dto.PostListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class PostRepositoryImpl implements PostRepositoryQuerydsl {

	private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager em) {

		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<PostListResponse> getPosts(Pageable pageable) {

		QPost post = QPost.post;
		QHashtag hashtag = QHashtag.hashtag;
		QPostHashtag postHashtag = QPostHashtag.postHashtag;

		List<PostListResponse> postList = queryFactory
				.select(
						Projections.bean(
								PostListResponse.class,
								post.id.as("postId"),
								post.type,
								post.title,
								post.content,
								post.viewCnt,
								post.likeCnt,
								post.shareCnt,
								post.createdAt,
								post.updatedAt
						)
				)
				.from(post)
				.leftJoin(postHashtag).on(postHashtag.post.id.eq(post.id))
				.leftJoin(hashtag).on(hashtag.id.eq(postHashtag.hashTag.id))
				.groupBy(post.id)
				.orderBy(post.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch()
				.stream()
				.map(postListResponse -> {
					List<String> hashtagList = queryFactory
							.select(hashtag.content)
							.from(postHashtag)
							.join(hashtag).on(hashtag.id.eq(postHashtag.hashTag.id))
							.where(postHashtag.post.id.eq(postListResponse.getPostId()))
							.fetch();
					postListResponse.setHashtagList(hashtagList);

					return postListResponse;
				})
				.collect(Collectors.toList());

		long total = queryFactory
				.selectFrom(post)
				.fetchCount();

		return new PageImpl<>(postList, pageable, total);
	}

}
