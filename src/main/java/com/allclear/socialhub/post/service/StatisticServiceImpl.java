package com.allclear.socialhub.post.service;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.common.util.DateUtil;
import com.allclear.socialhub.post.common.hashtag.repository.HashTagRepository;
import com.allclear.socialhub.post.common.like.repository.PostLikeRepository;
import com.allclear.socialhub.post.common.response.StatisticQueryResponse;
import com.allclear.socialhub.post.common.share.repository.PostShareRepository;
import com.allclear.socialhub.post.common.view.repository.PostViewRepository;
import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import com.allclear.socialhub.post.dto.StatisticResponse;
import com.allclear.socialhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private final HashTagRepository hashTagRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostShareRepository postShareRepository;
    private final PostViewRepository postViewRepository;

    /**
     * 1. 통계
     * 작성자 : 김효진, 김유현
     *
     * @param hashtag
     * @param type    : 일자별, 시간별
     * @param start   : start date
     * @param end     : end date
     * @param value   : count, like_count, share_count, view_count
     * @return List<StatisticDto>
     */
    @Override
    public List<StatisticResponse> getStatistics(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value) {

        // 0. 날짜 검증
        validateDateRange(type, start, end);

        // 1. 일자별 혹은 시간대별 날짜 포맷 패턴 설정
        String queryDateFormatPattern = getQueryDateFormatPattern(type);

        // 2. hashtag 테이블에서 해시태그 가진 게시물 리스트
        List<Long> postIds = hashTagRepository.getPostByHashtag(hashtag);

        // 3. start ~ end 날짜로 일자별 혹은 시간대별 개수를 가져오는 쿼리 날린 결과
        List<StatisticQueryResponse> queryResponses = getQueryResponsesByValue(value, postIds, start, end, queryDateFormatPattern);

        // 4. 시간 - 개수 Map으로 변환
        Map<String, Long> queryResponseMap = queryResponses.stream()
                .collect(Collectors.toMap(StatisticQueryResponse::getTime, StatisticQueryResponse::getValue, (existing, replacement) -> existing));

        // 5. 일자별 혹은 시간대별로 리스트 초기화
        List<StatisticResponse> responses = initializeStatistics(type, start, end);

        // 6. 시간 - 개수 Map에 존재하는 시간이면 개수를 매핑
        for (StatisticResponse statisticResponse : responses) {
            String time = statisticResponse.getTime();
            if (queryResponseMap.containsKey(time)) {
                statisticResponse.setValue(queryResponseMap.get(time));
            }
        }

        return responses;

    }

    /**
     * 날짜 유효성 검증 메소드
     * - 작성자 : 김유현
     *
     * @param type  통계 타입 (일자별, 시간대별)
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @throws CustomException 날짜 유효성 검증 실패 시 발생
     */
    private void validateDateRange(StatisticType type, LocalDate start, LocalDate end) {

        // end 날짜가 오늘보다 미래일 경우
        if (DateUtil.getDateDiff(end, LocalDate.now()) < 0) {
            throw new CustomException(ErrorCode.STATISTICS_INVALID_END_DATE_IN_FUTURE);
        }

        // start 날짜가 end 날짜보다 미래일 경우
        Long diff = DateUtil.getDateDiff(start, end);
        log.info("start ~ end : " + diff);
        if (diff < 0) {
            throw new CustomException(ErrorCode.STATISTICS_INVALID_DATE_RANGE);
        }

        // 통계 타입에 따라 최대 날짜 넘을 경우
        switch (type) {
            case DATE -> {
                if (diff > 30) {
                    throw new CustomException(ErrorCode.STATISTICS_INVALID_DATE_DURATION_DATE);
                }
            }
            case HOUR -> {
                if (diff > 7) {
                    throw new CustomException(ErrorCode.STATISTICS_INVALID_DATE_DURATION_HOUR);
                }
            }
        }
    }


    /**
     * 통계 값에 따라 쿼리 결과를 가져옵니다.
     * 작성자 : 김효진, 김유현
     *
     * @param value                  통계 값 (COUNT, LIKE_COUNT, VIEW_COUNT, SHARE_COUNT)
     * @param postIds                통계 계산에 사용할 게시물 ID 리스트
     * @param start                  통계 집계 시작 날짜
     * @param end                    통계 집계 종료 날짜
     * @param queryDateFormatPattern 날짜 포맷 패턴 (ex. '%Y-%m-%d', '%Y-%m-%d %H:%i')
     * @return List<StatisticQueryResponse> 통계 데이터 리스트
     */
    private List<StatisticQueryResponse> getQueryResponsesByValue(StatisticValue value, List<Long> postIds, LocalDate start, LocalDate end, String queryDateFormatPattern) {

        switch (value) {
            case COUNT -> {
                return postRepository.findStatisticByPostIds(postIds, start, end, queryDateFormatPattern);
            }
            case LIKE_COUNT -> {
                return postLikeRepository.findStatisticByPostIds(postIds, start, end, queryDateFormatPattern);

            }
            case VIEW_COUNT -> {
                return postViewRepository.findStatisticByPostIds(postIds, start, end, queryDateFormatPattern);

            }
            case SHARE_COUNT -> {
                return postShareRepository.findStatisticByPostIds(postIds, start, end, queryDateFormatPattern);
            }
            default -> {
                throw new IllegalArgumentException("잘못된 StatisticValue 입니다. value : " + value);
            }
        }
    }

    /**
     * 통계 유형에 따른 날짜 포맷 패턴을 반환합니다.
     * 작성자 : 김유현
     *
     * @param type 통계 유형 (일자별 또는 시간별)
     * @return String 날짜 포맷 패턴
     */
    private String getQueryDateFormatPattern(StatisticType type) {

        switch (type) {
            case DATE -> {
                return "%Y-%m-%d";
            }
            case HOUR -> {
                return "%Y-%m-%d %H:%i";
            }
            default -> {
                throw new IllegalArgumentException("잘못된 StatisticType 입니다. type : " + type);
            }
        }
    }

    /**
     * 통계 유형에 따라 일자별 또는 시간별 데이터를 초기화합니다.
     * 작성자 : 김유현
     *
     * @param type  통계 유형 (일자별 또는 시간별)
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @return List<StatisticResponse> 초기화된 통계 데이터 리스트
     */
    private List<StatisticResponse> initializeStatistics(StatisticType type, LocalDate start, LocalDate end) {

        switch (type) {
            case DATE -> {
                return initializeDailyStatistics(start, end);
            }
            case HOUR -> {
                return initializeHourlyStatistics(start, end);
            }
            default -> {
                throw new IllegalArgumentException("잘못된 StatisticType 입니다. type : " + type);
            }
        }
    }

    /**
     * 주어진 기간에 대해 일자별 통계를 초기화합니다.
     * 작성자 : 김효진
     *
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @return List<StatisticResponse> 초기화된 일자별 통계 데이터 리스트 ex. [{2024-08-24 : 0}, ...]
     */
    private List<StatisticResponse> initializeDailyStatistics(LocalDate start, LocalDate end) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<StatisticResponse> result = new ArrayList<>();

        List<LocalDate> localDates = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            // LocalDate를 문자열로 변환합니다.
            String formattedDate = date.format(formatter);
            StatisticResponse statisticResponse = new StatisticResponse(formattedDate, 0L);
            result.add(statisticResponse);
        }

        return result;

    }

    /**
     * 주어진 기간에 대해 시간별 통계를 초기화합니다.
     * 작성자 : 김유현
     *
     * @param start 시작 날짜
     * @param end   종료 날짜
     * @return List<StatisticResponse> 초기화된 시간별 통계 데이터 리스트 ex. [{2024-08-24 00:00 : 0}, ...]
     */
    public List<StatisticResponse> initializeHourlyStatistics(LocalDate start, LocalDate end) {

        // 1. LocalDate를 LocalDateTime으로 변환하여 시작 시간과 끝 시간을 설정
        //    시작 시간: start 날짜의 00:00 (하루의 시작)
        //    끝 시간: end 날짜의 23:00
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 0);

        // 2. 결과를 저장할 리스트를 초기화
        List<StatisticResponse> result = new ArrayList<>();

        // 3. 날짜와 시간의 포맷을 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 4. 시작 시간부터 끝 시간까지 1시간 단위로 반복
        for (LocalDateTime dateTime = startDateTime; !dateTime.isAfter(endDateTime); dateTime = dateTime.plusHours(1)) {
            // 5. 현재 LocalDateTime 객체를 문자열 형식으로 변환
            //    포맷된 문자열은 "yyyy-MM-dd HH:mm" 형태로, 시간 단위로 구분
            String formattedDateTime = dateTime.format(formatter);

            // 6. 포맷된 문자열과 초기 값 0L을 사용하여 StatisticResponse 객체를 생성
            StatisticResponse statisticResponse = new StatisticResponse(formattedDateTime, 0L);
            result.add(statisticResponse);
        }

        return result;
    }

}
