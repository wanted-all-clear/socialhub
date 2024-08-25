package com.allclear.socialhub.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {

    @Test
    @DisplayName("날짜_계산")
    void getDateDiff() {
        // given
        LocalDate start = LocalDate.of(2024, 8, 12);
        LocalDate end = LocalDate.of(2024, 8, 20);

        // when
        Long result = DateUtil.getDateDiff(start, end);

        // then
        assertEquals(result, 8);
    }

}
