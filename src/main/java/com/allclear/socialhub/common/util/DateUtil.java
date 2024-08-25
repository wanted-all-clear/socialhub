package com.allclear.socialhub.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static Long getDateDiff(LocalDate start, LocalDate end) {

        return ChronoUnit.DAYS.between(start, end);
    }

}
