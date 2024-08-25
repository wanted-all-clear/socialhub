package com.allclear.socialhub.post.repository.querydsl;

import com.allclear.socialhub.post.common.hashtag.domain.QHashtag;
import com.allclear.socialhub.post.common.hashtag.domain.QPostHashtag;
import com.allclear.socialhub.post.domain.QPost;
import com.allclear.socialhub.post.dto.PostDetailResponse;
import com.allclear.socialhub.post.dto.PostListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class PostRepositoryImpl implements PostRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    // 게시물 목록 조회
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
                .leftJoin(hashtag).on(hashtag.id.eq(postHashtag.hashtag.id))
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
                            .join(hashtag).on(hashtag.id.eq(postHashtag.hashtag.id))
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

    // 게시물 상세 조회
    public PostDetailResponse getPostDetail(Long postId, Long userId) {

        QPost post = QPost.post;
        QHashtag hashtag = QHashtag.hashtag;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;

        // 1. PostDetailResponse 쿼리 실행
        PostDetailResponse postDetailResponse = queryFactory
                .select(
                        Projections.constructor(
                                PostDetailResponse.class,
                                post.id.as("postId"),
                                post.user.id,
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
                .where(post.id.eq(postId))
                .fetchOne();

        // 2. 해시태그 리스트 쿼리 실행
        List<String> hashtagList = queryFactory
                .select(hashtag.content)
                .from(postHashtag)
                .join(hashtag).on(hashtag.id.eq(postHashtag.hashtag.id))
                .where(postHashtag.post.id.eq(postId))
                .fetch();

        // 3. PostDetailResponse 에 해시태그 리스트 저장
        postDetailResponse.setHashtagList(hashtagList);

        return postDetailResponse;
    }

}
