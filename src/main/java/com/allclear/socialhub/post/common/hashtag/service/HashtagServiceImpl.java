package com.allclear.socialhub.post.common.hashtag.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;
import com.allclear.socialhub.post.common.hashtag.repository.HashtagRepository;
import com.allclear.socialhub.post.common.hashtag.repository.PostHashtagRepository;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.allclear.socialhub.post.common.hashtag.domain.QHashtag.hashtag;
import static com.allclear.socialhub.post.common.hashtag.domain.QPostHashtag.postHashtag;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final EntityManager entityManager;

    /**
     * 해시태그 등록
     * 작성자 : 오예령
     *
     * @param hashtagList
     * @return
     */
    @Override
    @Transactional
    public List<Hashtag> createHashtag(List<String> hashtagList) {

        List<Hashtag> savedHashtags = new ArrayList<>();

        for (String content : hashtagList) {

            // 3. 해시태그 중복 체크 및 저장
            Hashtag hashtag = hashtagRepository.findByContent(content)
                    .orElseGet(() -> hashtagRepository.save(
                            Hashtag.builder()
                                    .content(content)
                                    .build()
                    ));

            savedHashtags.add(hashtag);
        }

        return savedHashtags;
    }

    /**
     * 해시태그 수정
     * 작성자 : 오예령
     *
     * @param postId
     * @param hashtagList
     * @return
     */
    @Override
    @Transactional
    public List<Hashtag> updateHashtag(Long postId, List<String> hashtagList) {

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPostId(postId);

        // 기존에 저장되어 있는 hastagList
        List<String> originHashtagList = new ArrayList<>();
        // 수정 요청한 hashtagList
        List<String> compareHashtagList = removeHashSymbol(hashtagList);

        for (PostHashtag postHashtag : postHashtags) {
            originHashtagList.add(postHashtag.getHashtag().getContent());
        }

        // newHashtagList에 originHashtagList를 복사
        List<String> newHashtagList = new ArrayList<>(originHashtagList);

        // 기존에 저장되어 있던 값을 새로 요청온 값과 비교하여 중복된 값 제거 (해당 게시물에서 삭제할 해시태그)
        originHashtagList.removeAll(compareHashtagList);

        // hashtag 테이블에서 content로 찾되, content가 origin 안에 있는 값과 일치하는 경우 출력되는 hastagId
        List<Long> ids = new JPAQueryFactory(entityManager)
                .select(hashtag.id)
                .from(hashtag)
                .where(hashtag.content.in(originHashtagList))
                .fetch();

        // 해시태그 수정 후 더이상 존재하지 않는 값 삭제
        deleteByPostIdAndHashtagIds(postId, ids);

        compareHashtagList.removeAll(newHashtagList);

        // 새로 추가해야 하는 해시태그 반환
        return createHashtag(compareHashtagList);
    }

    @Transactional
    public void deleteByPostIdAndHashtagIds(Long postId, List<Long> hashtagIds) {

        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, postHashtag);

        deleteClause.where(
                postHashtag.post.id.eq(postId)
                        .and(postHashtag.hashtag.id.in(hashtagIds))
        ).execute();
    }

    /**
     * 해시태그 검증 및 '#' 삭제
     * 작성자 : 오예령
     *
     * @param hashtagList
     * @return '#'가 삭제된 hashtagList 반환
     */
    public List<String> removeHashSymbol(List<String> hashtagList) {

        List<String> cleanedHashtagList = new ArrayList<>();
        for (String content : hashtagList) {
            // 1. 해시태그 형식 검증
            // '#'이 정확히 하나로 시작해야 하고, 그 뒤에 텍스트가 있어야 함
            if (!content.matches("^#[^#]+$")) {
                throw new CustomException(ErrorCode.INVALID_HASHTAG_PATTERN);
            }

            // 2. '#' 제거
            String cleanedContent = content.substring(1);
            cleanedHashtagList.add(cleanedContent);
        }
        return cleanedHashtagList;
    }

}
