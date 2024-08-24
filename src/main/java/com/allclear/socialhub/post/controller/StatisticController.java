package com.allclear.socialhub.post.controller;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import com.allclear.socialhub.post.dto.StatisticRequestParam;
import com.allclear.socialhub.post.dto.StatisticResponse;
import com.allclear.socialhub.post.service.StatisticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/posts/statistics")
@Slf4j
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public List<StatisticResponse> getStatistics(@Valid StatisticRequestParam statisticRequest) {
        
        // TODO: hashtag = null일 경우 본인계정으로 설정

        String hashtag = statisticRequest.getHashtag();
        StatisticType type = statisticRequest.getType();
        LocalDate start = statisticRequest.getStart();
        LocalDate end = statisticRequest.getEnd();
        StatisticValue value = statisticRequest.getValue();

        log.info("hashtag : " + hashtag);
        log.info("type : " + type);
        log.info(start + "~" + end);
        log.info("value : " + value);
        return statisticService.getStatistics(hashtag, type, start, end, value);
    }

}
