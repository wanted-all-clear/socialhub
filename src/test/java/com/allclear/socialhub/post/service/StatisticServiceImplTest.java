package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.post.common.like.repository.PostLikeRepository;
import com.allclear.socialhub.post.common.response.StatisticQueryResponse;
import com.allclear.socialhub.post.common.share.repository.PostShareRepository;
import com.allclear.socialhub.post.common.view.repository.PostViewRepository;
import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import com.allclear.socialhub.post.dto.StatisticResponse;
import com.allclear.socialhub.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticService 테스트")
class StatisticServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostViewRepository postViewRepository;

    @Mock
    private PostShareRepository postShareRepository;

    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Nested
    @DisplayName("날짜 범위가 유효한지 검증할 때")
    class WhenValidateDateRange {

        @Test
        @DisplayName("시작 날짜가 끝 날짜보다 미래일 경우 CustomException을 발생시킨다")
        void GivenStartDateAfterEndDate_ThenThrowCustomException() {
            // given
            StatisticType type = StatisticType.HOUR;
            LocalDate start = LocalDate.of(2024, 12, 2);
            LocalDate end = LocalDate.of(2024, 1, 1);

            // when, then
            assertThrows(CustomException.class, () -> {
                statisticService.validateDateRange(type, start, end);
            });
        }

        @Test
        @DisplayName("HOUR 타입일 때 날짜 범위가 7일을 초과하면 CustomException을 발생시킨다")
        void GivenHourTypeAndRangeExceeds7Days_ThenThrowCustomException() {
            // given
            StatisticType type = StatisticType.HOUR;
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 1, 9);

            // when, then
            assertThrows(CustomException.class, () -> {
                statisticService.validateDateRange(type, start, end);
            });
        }

        @Test
        @DisplayName("DATE 타입일 때 날짜 범위가 30일을 초과하면 CustomException을 발생시킨다")
        void GivenDateTypeAndRangeExceeds30Days_ThenThrowCustomException() {
            // given
            StatisticType type = StatisticType.DATE;
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 2, 2);

            // when, then
            assertThrows(CustomException.class, () -> {
                statisticService.validateDateRange(type, start, end);
            });
        }

    }

    @Nested
    @DisplayName("통계 유형에 따른 날짜 포맷 패턴이 제공될 때")
    class WhenQueryDateFormatPattern {

        @Test
        @DisplayName("통계 유형이 DATE일 경우, 날짜 포맷 패턴을 반환한다")
        void GivenStatisticTypeIsDate_ThenReturnDateFormatPattern() {

            // given
            StatisticType type = StatisticType.DATE;

            // when
            String result = statisticService.getQueryDateFormatPattern(type);

            // then
            assertEquals("%Y-%m-%d", result);
        }

        @Test
        @DisplayName("통계 유형이 HOUR일 경우, 날짜와 시간 포맷 패턴을 반환한다")
        void GivenStatisticTypeIsHour_ThenReturnHourFormatPattern() {
            // given
            StatisticType type = StatisticType.HOUR;

            // when
            String result = statisticService.getQueryDateFormatPattern(type);

            // then
            assertEquals("%Y-%m-%d %H:%i", result);
        }

    }

    @Nested
    @DisplayName("통계 유형에 따라 일자별 혹은 시간별 데이터를 초기화할 때")
    class WhenInitializeStatistics {

        @Test
        @DisplayName("DATE 타입을 주면 일자별 통계를 초기화한다")
        void GivenDateType_ThenInitializeDailyStatistics() {
            // given
            StatisticType type = StatisticType.DATE;
            LocalDate start = LocalDate.of(2024, 8, 1);
            LocalDate end = LocalDate.of(2024, 8, 3);

            // when
            List<StatisticResponse> result = statisticService.initializeStatistics(type, start, end);

            // then
            assertEquals(3, result.size());
            assertEquals("2024-08-01", result.get(0).getTime());
            assertEquals("2024-08-03", result.get(2).getTime());
            assertEquals(0L, result.get(0).getValue());
            assertEquals(0L, result.get(2).getValue());
        }

        @Test
        @DisplayName("HOUR 타입을 주면 시간별 통계를 초기화한다")
        void GivenHourType_ThenInitializeHourlyStatistics() {
            // given
            StatisticType type = StatisticType.HOUR;
            LocalDate start = LocalDate.of(2024, 8, 1);
            LocalDate end = LocalDate.of(2024, 8, 1);

            // when
            List<StatisticResponse> result = statisticService.initializeStatistics(type, start, end);

            // then
            assertEquals(24, result.size());
            assertEquals(24, result.size());
            assertEquals("2024-08-01 00:00", result.get(0).getTime());
            assertEquals("2024-08-01 23:00", result.get(23).getTime());
            assertEquals(0L, result.get(0).getValue());
            assertEquals(0L, result.get(23).getValue());
        }

    }

    @Nested
    @DisplayName("일자별 통계를 초기화할 때")
    class WhenInitializeDailyStatistics {

        @Test
        @DisplayName("유효한 기간을 주면 일자별 통계를 초기화한다")
        void GivenValidDateRange_ThenInitializeDailyStatistics() {
            // given
            LocalDate start = LocalDate.of(2024, 8, 1);
            LocalDate end = LocalDate.of(2024, 8, 3);

            // when
            List<StatisticResponse> result = statisticService.initializeDailyStatistics(start, end);

            // then
            assertEquals(3, result.size());
            assertEquals("2024-08-01", result.get(0).getTime());
            assertEquals("2024-08-03", result.get(2).getTime());
        }

    }

    @Nested
    @DisplayName("시간별 통계를 초기화할 때")
    class WhenInitializeHourlyStatistics {

        @Test
        @DisplayName("유효한 기간을 주면 시간별 통계를 초기화한다")
        void GivenValidDateRange_ThenInitializeHourlyStatistics() {
            // given
            LocalDate start = LocalDate.of(2024, 8, 1);
            LocalDate end = LocalDate.of(2024, 8, 1);

            // when
            List<StatisticResponse> result = statisticService.initializeHourlyStatistics(start, end);

            // then
            assertEquals(24, result.size());
            assertEquals("2024-08-01 00:00", result.get(0).getTime());
            assertEquals("2024-08-01 23:00", result.get(23).getTime());
        }

    }

    @Nested
    @DisplayName("StatisticQueryResponse 겍체 리스트를 시간 - 개수의 Map 으로 변환할 떄")
    class WhenConvertStatisticQueryResponsesToMap {

        @Test
        @DisplayName("StatisticQueryResponses 리스트를 주면 시간 - 개수 Map을 반환한다")
        void GivenStatisticQueryResponses_ThenReturnMap() {
            // given
            List<StatisticQueryResponse> queryResponses = Arrays.asList(
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-08-01", 3L),
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-08-03", 5L)
            );

            // when
            Map<String, Long> result = statisticService.convertStatisticQueryResponseToMap(queryResponses);

            // then
            assertEquals(2, result.size());
            assertEquals(3L, result.get("2024-08-01"));
            assertEquals(5L, result.get("2024-08-03"));
        }

    }

    @Nested
    @DisplayName("통계 값에 따라 쿼리 결과를 가져올 때")
    class WhenGetDaliyQueryReponsesByValue {

        @Test
        @DisplayName("통계 값이 COUNT인 경우 쿼리 결과들을 가져온다.")
        void GivenStatisticValueIsCount_ThenReturnStatisticQueryResponses() {
            // given
            StatisticValue value = StatisticValue.COUNT;
            List<Long> postIds = Arrays.asList(1L, 2L, 3L);
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 1, 31);
            String queryDateFormatPattern = "%Y-%m-%d";
            List<StatisticQueryResponse> queryResponses = Arrays.asList(
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-01", 3L),
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-05", 5L)
            );

            when(postRepository.findStatisticByPostIds(eq(postIds), eq(start), eq(end), eq(queryDateFormatPattern)))
                    .thenReturn(queryResponses);

            // when
            List<StatisticQueryResponse> result = statisticService.getQueryResponsesByValue(
                    value, postIds, start, end, queryDateFormatPattern
            );

            // then
            assertEquals(queryResponses, result);
        }

        @Test
        @DisplayName("통계 값이 LIKE_COUNT인 경우 쿼리 결과들을 가져온다.")
        void GivenStatisticValueIsLikeCount_ThenReturnStatisticQueryResponses() {
            // given
            StatisticValue value = StatisticValue.LIKE_COUNT;
            List<Long> postIds = Arrays.asList(1L, 2L, 3L);
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 1, 31);
            String queryDateFormatPattern = "%Y-%m-%d";
            List<StatisticQueryResponse> queryResponses = Arrays.asList(
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-01", 3L),
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-05", 5L)
            );

            when(postLikeRepository.findStatisticByPostIds(eq(postIds), eq(start), eq(end), eq(queryDateFormatPattern)))
                    .thenReturn(queryResponses);

            // when
            List<StatisticQueryResponse> result = statisticService.getQueryResponsesByValue(
                    value, postIds, start, end, queryDateFormatPattern
            );

            // then
            assertEquals(queryResponses, result);
        }

        @Test
        @DisplayName("통계 값이 VIEW_COUNT인 경우 쿼리 결과들을 가져온다.")
        void GivenStatisticValueIsViewCount_ThenReturnStatisticQueryResponses() {
            // given
            StatisticValue value = StatisticValue.VIEW_COUNT;
            List<Long> postIds = Arrays.asList(1L, 2L, 3L);
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 1, 31);
            String queryDateFormatPattern = "%Y-%m-%d";
            List<StatisticQueryResponse> queryResponses = Arrays.asList(
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-01", 3L),
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-05", 5L)
            );

            when(postViewRepository.findStatisticByPostIds(eq(postIds), eq(start), eq(end), eq(queryDateFormatPattern)))
                    .thenReturn(queryResponses);

            // when
            List<StatisticQueryResponse> result = statisticService.getQueryResponsesByValue(
                    value, postIds, start, end, queryDateFormatPattern
            );

            // then
            assertEquals(queryResponses, result);
        }

        @Test
        @DisplayName("통계 값이 SHARE_COUNT인 경우 쿼리 결과들을 가져온다.")
        void GivenStatisticValueIsShareCount_ThenReturnStatisticQueryResponses() {
            // given
            StatisticValue value = StatisticValue.SHARE_COUNT;
            List<Long> postIds = Arrays.asList(1L, 2L, 3L);
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 1, 31);
            String queryDateFormatPattern = "%Y-%m-%d";
            List<StatisticQueryResponse> queryResponses = Arrays.asList(
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-01", 3L),
                    StatisticServiceImplTestHelper.createStatisticQueryResponse("2024-01-05", 5L)
            );

            when(postShareRepository.findStatisticByPostIds(eq(postIds), eq(start), eq(end), eq(queryDateFormatPattern)))
                    .thenReturn(queryResponses);

            // when
            List<StatisticQueryResponse> result = statisticService.getQueryResponsesByValue(
                    value, postIds, start, end, queryDateFormatPattern
            );

            // then
            assertEquals(queryResponses, result);
        }

    }

    @Nested
    @DisplayName("쿼리 결과에서 가져온 시간-개수 맵을 사용하여 초기화된 통계 리스트를 업데이트할 때")
    class WhenUpdatingStatisticsWithQueryResults {

        @Test
        @DisplayName("쿼리 결과에 있는 시간으로 초기화된 통계 리스트가 올바르게 업데이트된다")
        void GivenQueryResponseMap_ThenUpdateStatistics() {
            // given
            List<StatisticResponse> statisticsResponses = Arrays.asList(
                    new StatisticResponse("2024-08-01", 0L),
                    new StatisticResponse("2024-08-02", 0L),
                    new StatisticResponse("2024-08-03", 0L)
            );

            Map<String, Long> queryResponseMap = new HashMap<>();
            queryResponseMap.put("2024-08-01", 3L);
            queryResponseMap.put("2024-08-03", 5L);

            // when
            List<StatisticResponse> result = statisticService.updateStatisticsWithQueryResults(queryResponseMap, statisticsResponses);

            // then
            assertEquals(3, result.size());
            assertEquals(3L, result.get(0).getValue()); // 2024-08-01
            assertEquals(0L, result.get(1).getValue()); // 2024-08-02
            assertEquals(5L, result.get(2).getValue()); // 2024-08-03
        }

        @Test
        @DisplayName("쿼리 결과가 비어있을 때 초기화된 통계 리스트는 변경되지 않는다")
        void GivenEmptyQueryResponseMap_ThenStatisticsNotChanged() {
            // given
            List<StatisticResponse> statisticsResponses = Arrays.asList(
                    new StatisticResponse("2024-08-01", 0L),
                    new StatisticResponse("2024-08-02", 0L),
                    new StatisticResponse("2024-08-03", 0L)
            );

            Map<String, Long> queryResponseMap = Collections.emptyMap();

            // when
            List<StatisticResponse> result = statisticService.updateStatisticsWithQueryResults(queryResponseMap, statisticsResponses);

            // then
            assertEquals(3, result.size());
            assertEquals(0L, result.get(0).getValue()); // 2024-08-01
            assertEquals(0L, result.get(1).getValue()); // 2024-08-02
            assertEquals(0L, result.get(2).getValue()); // 2024-08-03
        }

    }


}
