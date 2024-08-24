package com.allclear.socialhub.post.service;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final HashTagRepository hashTagRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostShareRepository postShareRepository;
    private final PostViewRepository postViewRepository;

    /**
     * 1. 일자별 통계
     * 작성자 : 김효진
     *
     * @param hashtag
     * @param type    : 일자별, 시간별
     * @param start   : start date
     * @param end     : end date
     * @param value   : count, like_count, share_count, view_count
     * @return List<StatisticDto>
     */
    @Override
    public List<StatisticResponse> getStatisticsDaily(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value) {

        List<Long> postIds = hashTagRepository.getPostByHashtag(hashtag);
        List<StatisticQueryResponse> responses = new ArrayList<>();
        if (value.equals(StatisticValue.COUNT)) {
            responses = postRepository.findDailyStatisticByPostIds(postIds, start, end);
        } else if (value.equals(StatisticValue.LIKE_COUNT)) {
            responses = postLikeRepository.findDailyStatisticByPostIds(postIds, start, end);
        } else if (value.equals(StatisticValue.SHARE_COUNT)) {
            responses = postShareRepository.findDailyStatisticByPostIds(postIds, start, end);
        } else if (value.equals(StatisticValue.VIEW_COUNT)) {
            responses = postViewRepository.findDailyStatisticByPostIds(postIds, start, end);
        }

        List<StatisticResponse> daily = getDaily(start, end);
        for (StatisticResponse statisticResponse : daily) {
            String time = statisticResponse.getTime();
            List<StatisticQueryResponse> filteredList = responses.stream().filter(it -> it.getTime().equals(time)).collect(Collectors.toList());
            if (filteredList.size() > 0) {
                statisticResponse.setValue(filteredList.get(0).getValue());
            }
        }

        return daily;

    }

    private List<StatisticResponse> getDaily(LocalDate start, LocalDate end) {

        List<StatisticResponse> result = new ArrayList<>();

        List<LocalDate> localDates = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // LocalDate를 문자열로 변환합니다.
            String formattedDate = date.format(formatter);
            StatisticResponse statisticResponse = new StatisticResponse(formattedDate, 0L);
            result.add(statisticResponse);
        }

        return result;

    }

    /**
     * 2. 시간대별 통계
     * 작성자 : 김유현
     *
     * @param hashtag
     * @param type    : 일자별, 시간별
     * @param start   : start date
     * @param end     : end date
     * @param value   : count, like_count, share_count, view_count
     * @return List<StatisticDto>
     */
    @Override
    public List<StatisticResponse> getStatisticsHourly(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value) {

        return null;
    }

}
