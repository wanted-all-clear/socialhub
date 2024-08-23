package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface StatisticService {

    void getStatisticsDaily(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value);

    void getStatisticsHourly(String hashtag, StatisticType type, LocalDate start, LocalDate end, StatisticValue value);

}
