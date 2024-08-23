package com.allclear.socialhub.post.service;

import org.springframework.stereotype.Service;

@Service
public interface StatisticService {
    void getStatisticsDaily();
    void getStatisticsHourly();
}
