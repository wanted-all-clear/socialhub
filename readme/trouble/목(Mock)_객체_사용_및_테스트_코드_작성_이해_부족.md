### (1) 문제 상황
```java
1. JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
2. jwtTokenProvider.createToken(); 의 차이를 이해하지 못하여 테스트 코드 작성 시 시간이 많이 소요되었습니다.
```

## (2) 발생한 문제(에러)
>- `extractEmail`이나 `extractAllClaims` 등의 모킹이 제대로 설정되지 않음
- 이메일을 추출하는 과정에서 `null` 반환


## 3. 원인
- 추정되는 원인
> - 실제 로그인한 유저의 token을 받아와야 한다고 생각
> - 어노테이션의 설정 오류라고 생각: `@Mock`, `@MockBean`, `@AutoConfigureMockMvc`
- 실제 원인
> **실제 로직 vs 모킹된 로직**
  - 1번은 해당 클래스의 메서드를 가짜로 구현하여 실제로 아무런 로직을 수행하지 않고, 반환할 값이나 예외를 지정할 수 있습니다.
  - 2번은 실제 로직을 실행합니다.
---
1. `JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);`
- 실제로 JwtTokenProvider의 모든 메서드를 실제 구현 없이 스터빙(stubbing)할 수 있는 목 객체를 생성
- 이 객체는 JwtTokenProvider 클래스의 인스턴스처럼 동작하지만, 실제 로직은 수행되지 않습니다.

2. `jwtTokenProvider.createToken();`
  - 실제의 JWT 토큰 생성이 테스트에서 중요한 부분이라면, 
  - jwtTokenProvider.createToken(user)를 사용합니다.
---

## (4) 해결 방법
- `JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);` 으로 사용

## (5) 참고 자료
- [mock() vs @Mock vs @MockBean 이제 그만 헷갈리자!](https://simgee.tistory.com/58)
- **실제 JWT 토큰 생성이 필요 없는 이유**
  - 테스트에서 JWT 토큰이 어떻게 생성되는지 자체는 중요한 부분이 아닙니다.
  - 단지 토큰에서 올바른 이메일이 추출되고, 그 결과가 이메일 전송 로직으로 이어지는지, 그리고 예외가 발생할 때 올바르게 처리되는지에만 집중하면 됩니다.

- **목 객체(Mock Object)**
  - 단위 테스트에서 사용되는 가짜 객체

- **스터빙(stubbing)**
  - 목 객체가 어떤 메소드를 호출할 때, 실제 메소드의 동작을 대신하여 미리 정해진 값을 반환하거나, 특정 동작을 수행하도록 하는 과정
