package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import com.allclear.socialhub.post.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/posts/statistics")
@Slf4j
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public void getStatistics(
            @RequestParam(name = "hashtag", required = false) String hashtag,
            @RequestParam(name = "type", defaultValue = "date") StatisticType type,
            @RequestParam(name = "start", defaultValue = "#{T(java.time.LocalDate).now().minusDays(7)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(name = "value", defaultValue = "count") StatisticValue value
    ) {

        // 예외처리
        // 유효성검사
        // TODO: hashtag = null일 경우 본인계정으로 설정
        log.info("hashtag : " + "hashtag");

        log.info("type : " + type);
        log.info(start + "~" + end);
        log.info("value : " + value);
        if (type == StatisticType.DATE) {
            statisticService.getStatisticsDaily(hashtag, type, start, end, value);
        } else if (type == StatisticType.HOUR) {
            statisticService.getStatisticsHourly(hashtag, type, start, end, value);
        }
    }

}
