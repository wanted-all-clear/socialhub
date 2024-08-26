package com.allclear.socialhub.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("DateUtil 테스트")
class DateUtilTest {

    @Nested
    @DisplayName("날짜의 차이를 계산할때")
    class WhenGetDateDiff {

        @Test
        @DisplayName("시작 날짜가 끝 날짜보다 이전이면 양수를 반환한다")
        void GivenStartBeforeEnd_returnPositiveNumber() {
            // given
            LocalDate start = LocalDate.of(2024, 8, 12);
            LocalDate end = LocalDate.of(2024, 8, 20);

            // when
            Long result = DateUtil.getDateDiff(start, end);

            // then
            assertEquals(result, 8);
        }

        @Test
        @DisplayName("시작 날짜가 끝 날짜보다 이후이면 음수를 반환한다")
        void GivenStartAfterEnd_returnNegativeNumber() {
            // given
            LocalDate start = LocalDate.of(2024, 8, 20);
            LocalDate end = LocalDate.of(2024, 8, 12);

            // when
            Long result = DateUtil.getDateDiff(start, end);

            // then
            assertEquals(result, -8);
        }

    }

}
