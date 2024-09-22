package com.allclear.socialhub.common.util;

public class TokenUtil {

    public static String removeBearer(String token) {

        return token.substring(7);
    }

}
