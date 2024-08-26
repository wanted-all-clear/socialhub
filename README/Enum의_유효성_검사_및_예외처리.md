## (1) 문제 상황
> 쿼리 파라미터가 길고 복잡하여 DTO로 생성하였습니다.  
추가로 잘못된 Enum 값이 들어올 경우, 해당 타입에 따라서 원하는 에러 코드를 던지고 싶었습니다.

## (2) 발생한 문제(에러)
> 어떤 Enum값이든 상관없이 유효하지 않은 타입이 왔을 때 `BindException`을 던져서 언제 어느 타입이 유효하지 않은지 메시지를 정확히 주기 어려웠습니다.

## (3) 원인
> DTO에서 들어올 때, `@ModelAttribute`에서 타입이 맞지 않으면 `BindException`을 내기 때문이었습니다.

## (4) 최종 해결
> Global Exception Handler에서 파라미터명에 따라서 에러 코드를 매핑해주었습니다.  

## (5) 해결 방법

```java
// 우선 Global Exception Handler에서 파라미터명에 따라서 에러 코드를 매핑해주었다.
// 다만,  다른 메서드에서도 동일한 파라미터명이 들어올 수 있기 때문에 이를 처리해야한다.
  
@ExceptionHandler(value = {BindException.class})
protected ResponseEntity<ErrorResponse> handleBindException(BindException ex, HandlerMethod handlerMethod) {

    log.error("handleBindException", ex);

    FieldError fieldError = ex.getFieldError();
    String fieldName = fieldError.getField();
    Object rejectedValue = fieldError.getRejectedValue();

    String errorMessage = switch (fieldName) {
        case "type" -> ErrorCode.STATISTICS_INVALID_TYPE.getMessage();
        case "value" -> ErrorCode.STATISTICS_INVALID_VALUE.getMessage();
        case "start", "end" -> ErrorCode.STATISTICS_INVALID_DATE.getMessage();
        default -> fieldError.getDefaultMessage();
    };

    ErrorResponse response = ErrorResponse.create()
            .message(errorMessage)
            .httpStatus(HttpStatus.BAD_REQUEST);

    return ResponseEntity.badRequest().body(response);
}
```
## (6) 개선 방안
> - 컨트롤러나 메소드 명에 따라서, 의도한 에러코드를 낼 수 있도록 수정하거나,
커스텀 어노테이션을 만들어 Enum을 유효성 검사할 수 있도록 해야겠다.

## (7) 참고 자료
- [Enum 유효성 검사하기](https://cchoimin.tistory.com/entry/Enum-유효성-검사하기)
- [Enum Validation](https://tommykim.tistory.com/20)
