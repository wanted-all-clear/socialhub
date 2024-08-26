package com.allclear.socialhub.post.repository.querydsl;

import com.allclear.socialhub.post.common.hashtag.domain.QHashtag;
import com.allclear.socialhub.post.common.hashtag.domain.QPostHashtag;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.domain.PostType;
import com.allclear.socialhub.post.domain.QPost;
import com.allclear.socialhub.post.dto.PostDetailResponse;
import com.allclear.socialhub.post.domain.SearchByType;
import com.allclear.socialhub.post.dto.PostListResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.allclear.socialhub.post.common.hashtag.domain.QHashtag.hashtag;
import static com.allclear.socialhub.post.common.hashtag.domain.QPostHashtag.postHashtag;
import static com.allclear.socialhub.post.domain.QPost.post;

@Slf4j
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

    public Page<PostListResponse> searchPosts(Pageable pageable, String username, String hashtagQuery, PostType type, String query, String orderBy, String sort, String searchBy) {

        // 기본 쿼리 설정을 위한 메소드 호출
        JPAQuery<Post> queryBase = buildBaseQuery(username, hashtagQuery, type, query, searchBy);

        // 정렬 조건 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(orderBy, sort);
        queryBase.orderBy(orderSpecifier);

        // 페이징 및 데이터 조회
        List<Post> posts = queryBase
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Post 엔티티를 PostListResponse로 변환
        List<PostListResponse> postList = posts.stream()
                .map(this::mapToPostListResponse)
                .collect(Collectors.toList());

        // 총 게시물 수 조회
        long total = queryBase.fetchCount();

        // 결과를 Page 형태로 반환
        return new PageImpl<>(postList, pageable, total);
    }

    // 기본 쿼리 설정 메소드 (해당 게시물에서 조건 필터링 진행)
    private JPAQuery<Post> buildBaseQuery(String username, String hashtagQuery, PostType type, String query, String searchBy) {

        JPAQuery<Post> queryBase = queryFactory
                .selectFrom(post)
                .leftJoin(postHashtag).on(postHashtag.post.id.eq(post.id))
                .leftJoin(hashtag).on(hashtag.id.eq(postHashtag.hashtag.id))
                .distinct();

        // 해시태그 필터 적용
        if (hashtagQuery != null && !hashtagQuery.isEmpty()) {
            queryBase.where(hashtag.content.eq(hashtagQuery));
        } else if (username != null && !username.isEmpty()) {
            // 해시태그가 없으면 username으로 검색
            queryBase.where(post.user.username.eq(username));
        }

        // 게시물 타입 필터 적용
        if (type != null) {
            queryBase.where(post.type.eq(type));
        }

        // 검색 필터 적용 (searchBy에 따라 title 또는 content에서 검색, 미입력 시 title과 content 모두에서 검색)
        applySearchFilter(queryBase, query, searchBy);

        return queryBase;
    }

    // 검색 필터 적용 메소드
    private void applySearchFilter(JPAQuery<Post> queryBase, String query, String searchBy) {

        log.info("searchBy : {}", searchBy);
        EnumSet<SearchByType> searchByTypes = SearchByType.fromString(searchBy);

        if (searchByTypes.isEmpty()) {
            queryBase.where(post.title.containsIgnoreCase(query)
                    .or(post.content.containsIgnoreCase(query)));
        } else {
            if (searchByTypes.contains(SearchByType.TITLE)) {
                queryBase.where(post.title.containsIgnoreCase(query));
            }

            if (searchByTypes.contains(SearchByType.CONTENT)) {
                queryBase.where(post.content.containsIgnoreCase(query));
            }
        }
    }

    // 정렬 조건 설정 메소드
    private OrderSpecifier<?> getOrderSpecifier(String orderBy, String sort) {

        switch (orderBy) {
            case "created_at":
                return "asc".equalsIgnoreCase(sort) ? post.createdAt.asc() : post.createdAt.desc();
            case "updated_at":
                return "asc".equalsIgnoreCase(sort) ? post.updatedAt.asc() : post.updatedAt.desc();
            case "like_count":
                return "asc".equalsIgnoreCase(sort) ? post.likeCnt.asc() : post.likeCnt.desc();
            case "share_count":
                return "asc".equalsIgnoreCase(sort) ? post.shareCnt.asc() : post.shareCnt.desc();
            case "view_count":
                return "asc".equalsIgnoreCase(sort) ? post.viewCnt.asc() : post.viewCnt.desc();
            default:
                return post.id.desc(); // 기본 정렬: Id 내림차순
        }
    }

    // Post 엔티티를 PostListResponse로 변환하는 메소드
    private PostListResponse mapToPostListResponse(Post postEntity) {

        List<String> hashtags = queryFactory
                .select(hashtag.content)
                .from(postHashtag)
                .join(hashtag).on(hashtag.id.eq(postHashtag.hashtag.id))
                .where(postHashtag.post.id.eq(postEntity.getId()))
                .fetch();

        return PostListResponse.builder()
                .postId(postEntity.getId())
                .type(postEntity.getType())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .viewCnt(postEntity.getViewCnt())
                .likeCnt(postEntity.getLikeCnt())
                .shareCnt(postEntity.getShareCnt())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .hashtagList(hashtags)
                .build();
    }

}
