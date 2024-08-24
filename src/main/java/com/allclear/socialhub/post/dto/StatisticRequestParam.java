package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class StatisticRequestParam {

    private String hashtag;

    private StatisticType type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private StatisticValue value;

    public StatisticRequestParam(String hashtag,
                                 StatisticType type,
                                 LocalDate start,
                                 LocalDate end,
                                 StatisticValue value) {

        // TODO: hashtag = null일 경우 본인계정으로 설정
        this.hashtag = hashtag == null ? "본인계정" : hashtag;
        this.type = type == null ? StatisticType.DATE : type;
        this.start = start == null ? LocalDate.now().minusDays(7) : start;
        this.end = end == null ? LocalDate.now() : end;
        this.value = value == null ? StatisticValue.COUNT : value;
    }

}
