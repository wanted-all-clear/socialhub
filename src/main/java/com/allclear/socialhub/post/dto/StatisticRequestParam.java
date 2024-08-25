package com.allclear.socialhub.post.dto;

import com.allclear.socialhub.post.domain.StatisticType;
import com.allclear.socialhub.post.domain.StatisticValue;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class StatisticRequestParam {

    private String hashtag;

    private StatisticType type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate end;

    private StatisticValue value;


    public StatisticRequestParam(String hashtag,
                                 StatisticType type,
                                 LocalDate start,
                                 LocalDate end,
                                 StatisticValue value) {

        this.hashtag = hashtag;
        this.type = type == null ? StatisticType.DATE : type;
        this.start = start == null ? LocalDate.now().minusDays(7) : start;
        this.end = end == null ? LocalDate.now() : end;
        this.value = value == null ? StatisticValue.COUNT : value;
    }


}
