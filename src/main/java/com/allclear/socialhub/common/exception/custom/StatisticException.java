package com.allclear.socialhub.common.exception.custom;

import com.allclear.socialhub.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class StatisticException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public StatisticException(ErrorCode errorCode) {

        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
