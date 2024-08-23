package com.allclear.socialhub.common.util.converter;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.post.domain.StatisticValue;
import org.springframework.core.convert.converter.Converter;

public class StringToStatisticValueConverter implements Converter<String, StatisticValue> {

    @Override
    public StatisticValue convert(String source) {

        try {
            return StatisticValue.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.STATISTICS_INVALID_VALUE);
        }
    }

}
