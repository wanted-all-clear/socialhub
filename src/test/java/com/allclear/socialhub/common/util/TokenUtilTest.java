package com.allclear.socialhub.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.allclear.socialhub.auth.util.TokenUtil;

class TokenUtilTest {

    @Test
    @DisplayName("토큰 제거")
    void removeBearer() {
        // given
        String header = "Bearer sometokenssssss";

        // when
        String result = TokenUtil.removeBearer(header);

        // then
        assertEquals(result, "sometokenssssss");
    }

}
