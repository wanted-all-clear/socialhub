package com.allclear.socialhub.common.converter;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.domain.StatisticType;
import org.springframework.core.convert.converter.Converter;

public class StringToStatisticTypeConverter implements Converter<String, StatisticType> {

    @Override
    public StatisticType convert(String source) {

        try {
            return StatisticType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.STATISTICS_INVALID_TYPE);
        }
    }

}
