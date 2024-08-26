package com.allclear.socialhub.post.common.hashtag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "/statistics-data.sql")
class HashtagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Nested
    @DisplayName("해시태그 기반 게시물 조회")
    class WhenFindingPostsByHashtag {

        @Test
        @DisplayName("해시태그가 존재하는 경우 게시물 ID 목록을 반환한다.")
        void GivenExistingHashtag_WhenFindingPosts_ThenReturnsPostIds() {
            // given
            String hashtag = "OOTD";

            // when
            List<Long> results = hashtagRepository.getPostByHashtag(hashtag);

            // then
            assertEquals(5, results.size());
            assertEquals(1, results.get(0));
            assertEquals(2, results.get(1));
            assertEquals(3, results.get(2));
            assertEquals(4, results.get(3));
            assertEquals(5, results.get(4));
        }

        @Test
        @DisplayName("해시태그가 존재하지 않는 경우 빈 목록을 반환한다.")
        void GivenNonExistingHashtag_WhenFindingPosts_ThenReturnsEmptyList() {
            // given
            String hashtag = "인턴십";

            // when
            List<Long> results = hashtagRepository.getPostByHashtag(hashtag);

            // then
            assertEquals(0, results.size());
        }

    }

}
