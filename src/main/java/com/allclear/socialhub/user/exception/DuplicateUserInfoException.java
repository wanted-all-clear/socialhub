package com.allclear.socialhub.user.exception;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;

public class DuplicateUserInfoException extends CustomException {

    public DuplicateUserInfoException(ErrorCode errorCode) {

        super(errorCode);
    }

}
