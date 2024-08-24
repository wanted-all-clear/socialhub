package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import com.allclear.socialhub.post.dto.StatisticResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StatisticService {

    List<StatisticResponse> getStatisticsDaily(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value);

    List<StatisticResponse> getStatisticsHourly(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value);

}
