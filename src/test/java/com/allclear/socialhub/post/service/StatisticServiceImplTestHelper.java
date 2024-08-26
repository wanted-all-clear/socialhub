package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.common.response.StatisticQueryResponse;

public class StatisticServiceImplTestHelper {

    public static StatisticQueryResponse createStatisticQueryResponse(String time, Long value) {

        return new StatisticQueryResponse() {
            @Override
            public String getTime() {

                return time;
            }

            @Override
            public Long getValue() {

                return value;
            }
        };
    }

}
