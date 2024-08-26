package com.allclear.socialhub.post.common.share.repository;

import com.allclear.socialhub.post.common.response.StatisticQueryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = "/statistics-data.sql")
class PostShareRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostShareRepository postShareRepository;

    @Nested
    @DisplayName("통계 조회")
    class WhenFindStatistics {

        @Test
        @DisplayName("주어진 날짜 범위 내에서 일자별 통계를 조회하면 결과를 반환한다")
        void GivenValidDateRange_ThenReturnStatisticQueryResponses() {
            // given
            List<Long> postIds = List.of(1L, 3L);
            LocalDate startDate = LocalDate.of(2024, 8, 23);
            LocalDate endDate = LocalDate.of(2024, 8, 25);
            String dateFormatPattern = "%Y-%m-%d";

            // when
            List<StatisticQueryResponse> results = postShareRepository.findStatisticByPostIds(postIds, startDate, endDate, dateFormatPattern);

            // then
            assertEquals(2, results.size());
            assertEquals("2024-08-23", results.get(0).getTime());
            assertEquals(1L, results.get(0).getValue());
            assertEquals("2024-08-24", results.get(1).getTime());
            assertEquals(2L, results.get(1).getValue());
        }

        @Test
        @DisplayName("주어진 날짜와 시간 범위 내에서 시간별 통계를 조회하면 결과를 반환한다")
        void GivenValidDateAndTimeRange_ThenReturnStatisticQueryResponses() {
            // given
            List<Long> postIds = List.of(1L, 2L, 3L);
            LocalDate startDate = LocalDate.of(2024, 8, 23);
            LocalDate endDate = LocalDate.of(2024, 8, 24);
            String dateFormatPattern = "%Y-%m-%d %H:00";

            // when
            List<StatisticQueryResponse> results = postShareRepository.findStatisticByPostIds(postIds, startDate, endDate, dateFormatPattern);

            // then
            assertEquals(3, results.size());
            assertEquals("2024-08-23 12:00", results.get(0).getTime());
            assertEquals(1L, results.get(0).getValue());
            assertEquals("2024-08-24 13:00", results.get(1).getTime());
            assertEquals(2L, results.get(1).getValue());
            assertEquals("2024-08-24 14:00", results.get(2).getTime());
            assertEquals(1L, results.get(2).getValue());
        }

    }

}
