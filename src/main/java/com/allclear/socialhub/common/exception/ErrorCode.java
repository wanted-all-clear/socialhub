package com.allclear.socialhub.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // USER
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "가입되지 않은 아이디입니다."),
    USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST,  "중복된 아이디입니다."),
    PASSWORD_NOT_VALID (HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST,  "비밀번호는 10자 이상 16자 이하의 길이의 영문자, 숫자, 특수문자(!@#$%^&*)만 사용 가능합니다."),

    // POST
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    POST_NOT_OWNER(HttpStatus.BAD_REQUEST, "본인 글만 수정/삭제가 가능합니다."),
    POST_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물 타입입니다."),

    // STATISTICS
    STATISTICS_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 통계 타입입니다. expected: [date, hour]"),
    STATISTICS_VALUE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 통계 데이터 타입입니다. expected: [count, view_count, like_count, share_count]"),
    STATISTICS_INVALID_DATE_PATTERN(HttpStatus.BAD_REQUEST, "날짜 형식이 올바르지 않습니다. expected: YY-MM-DD"),
    STATISTICS_INVALID_DATE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 날짜입니다."),
    STATISTICS_INVALID_DATE_DURATION_DATE (HttpStatus.BAD_REQUEST, "최대 30일까지만 조회할 수 있습니다."),
    STATISTICS_INVALID_DATE_DURATION_HOUR (HttpStatus.BAD_REQUEST, "최대 7일까지만 조회할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus,  String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
