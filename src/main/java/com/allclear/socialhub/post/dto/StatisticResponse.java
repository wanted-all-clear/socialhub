package com.allclear.socialhub.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticResponse {

    private String time;
    private Long value;

}


