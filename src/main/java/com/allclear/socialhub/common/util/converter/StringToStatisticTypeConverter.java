package com.allclear.socialhub.common.util.converter;

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.common.exception.custom.StatisticException;
import com.allclear.socialhub.post.domain.StatisticType;
import org.springframework.core.convert.converter.Converter;

public class StringToStatisticTypeConverter implements Converter<String, StatisticType> {

    @Override
    public StatisticType convert(String source) {

        try {
            return StatisticType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new StatisticException(ErrorCode.STATISTICS_INVALID_TYPE);
        }
    }

}
