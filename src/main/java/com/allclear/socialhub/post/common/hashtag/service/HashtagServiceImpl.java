package com.allclear.socialhub.post.common.hashtag.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import com.allclear.socialhub.post.common.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public List<Hashtag> toEachHashtag(List<String> hashtagList) {

        List<Hashtag> savedHashtags = new ArrayList<>();

        for (String content : hashtagList) {

            // 1. 해시태그 형식 검증
            // '#'이 정확히 하나로 시작해야 하고, 그 뒤에 텍스트가 있어야 함
            if (!content.matches("^#[^#]+$")) {
                throw new CustomException(ErrorCode.INVALID_HASHTAG_PATTERN);
            }

            // 2. '#' 제거
            String cleanedContent = content.substring(1);

            // 3. 해시태그 중복 체크 및 저장
            Hashtag hashtag = hashtagRepository.findByContent(cleanedContent)
                    .orElseGet(() -> hashtagRepository.save(
                            Hashtag.builder()
                                    .content(cleanedContent)
                                    .build()
                    ));

            savedHashtags.add(hashtag);
        }

        return savedHashtags;
    }

}
