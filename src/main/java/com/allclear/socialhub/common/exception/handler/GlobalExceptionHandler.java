package com.allclear.socialhub.common.exception.handler;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {

        log.error("handleHttpRequestMethodNotSupportedException", ex);

        final ErrorResponse response
                = ErrorResponse.create()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .message(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        log.error("handleIllegalArgumentException", ex);

        ErrorResponse response = ErrorResponse.create()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorResponse> handleNullPointException(NullPointerException ex) {

        log.error("handleNullPointException : {}", ex.getMessage());

        ex.printStackTrace();
        ErrorResponse response = ErrorResponse.create()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<ErrorResponse> handleBindException(BindException ex) {

        log.error("handleBindException", ex);

        FieldError fieldError = ex.getFieldError();

        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();

        // TODO: 하드 코딩 변환할 방법 & 메서드의 type 들어올 경우 변경해야 함.
        String errorMessage = switch (fieldName) {
            case "type" -> ErrorCode.STATISTICS_INVALID_TYPE.getMessage();
            case "value" -> ErrorCode.STATISTICS_INVALID_VALUE.getMessage();
            case "start", "end" -> ErrorCode.STATISTICS_INVALID_DATE_PATTERN.getMessage();
            default -> fieldError.getDefaultMessage();
        };


        ErrorResponse response = ErrorResponse.create()
                .message(errorMessage)
                .httpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {

        log.error("handleCustomException", ex);

        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        ErrorResponse response
                = ErrorResponse
                .create()
                .message(message)
                .httpStatus(errorCode.getHttpStatus());

        return ResponseEntity.badRequest().body(response);
    }

}
