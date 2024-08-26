# Socialhub

> ## 목차
> 1. [서비스 소개](#서비스-소개)
> 2. [R&R](#rr)
> 3. [Issue & Jira 를 통한 트래킹 일정 관리](#issue--jira-를-통한-트래킹-일정-관리)
> 4. [Discord 이용한 소통 및 PR 알림 봇](#discord-이용한-소통-및-pr-알림-봇)
> 5. [협업 및 커뮤니케이션](#협업-및-커뮤니케이션)
>   - [Notion](#notion)
> 6. [주요 기능](#주요-기능)
> 7. [프로젝트 환경](#프로젝트-환경)
> 8. [요구사항 정의서 정리](#요구사항-정의서-정리)
> 9. [API 명세서](#api-명세서)
> 10. [ERD](#erd)
> 11. [트러블 슈팅](#트러블-슈팅)
>    - [JWT 토큰 시크릿 키 보안 오류](#jwt-토큰-시크릿-키-보안-오류)
>    - [Enum의 유효성 검사 및 예외처리](#enum의-유효성-검사-및-예외처리)
>    - [목(Mock) 객체 사용 및 테스트 코드 이해 부족](#목mock-객체-사용-및-테스트-코드-이해-부족)

<br/>

## 서비스 소개

해시태그를 기반으로 `인스타그램`, `스레드`, `페이스북`, `트위터(X)` 등
복수의 SNS에 게시된 게시물 중 해시태그가 포함된 게시물들을 하나의 서비스에서 확인할 수 있는
**통합 Feed 어플리케이션의 API 서버**입니다.

### 주요 기능

- 유저는 계정(추후 해시태그로 관리), 비밀번호, 이메일로 **가입요청**을 진행합니다.
- 가입 요청 시, 이메일로 발송된 코드를 입력하여 **가입승인**을 받고 서비스 이용이 가능합니다.
- 서비스 로그인 시, 메뉴는 **통합 Feed** 단일 입니다. 
- 통합 Feed 에선  `인스타그램`, `스레드`, `페이스북`, `트위터` 에서 유저의 계정이 태그된 글들을 확인합니다.
- 또는, 특정 해시태그(1건)를 입력하여, 해당 해시태그가 포함된 게시물들을 확인합니다.
- 유저는 본인 계정명 또는 특정 해시태그 일자별, 시간별 게시물 갯수 통계를 확인할 수 있습니다.
- 유저는 하나의 채널로 유저(ex. `#dani`), 또는 브랜드(ex. `#danishop`) 의 SNS 노출 게시물 및 통계를 확인할 수 있습니다.

<br/>

### 👩🏻‍💻 R&R
    - 오예령(팀장) : 게시물 기능 구현(등록, 수정, 삭제, 검색)
    - 유리빛나 : 게시물 기능 구현 (목록 조회, 상세 조회, 좋아요, 공유)
    - 김유현 : 통계 기능 구현 (서비스 및 컨트롤러, 단위 테스트)
    - 김은정 : 사용자 기능 구현 (로그인, 계정 중복 확인)   
    - 김효진 : 통계 기능 구현 (서비스 및 레포지토리, 스웨거)
    - 배서진 : 사용자 기능 구현 (회원가입, 이메일 인증 요청&검증)

### 🗣️ 협업 및 커뮤니케이션

<details>
<summary>문서화 작업</summary>
<div markdown="1">
<figure class="half">  
    <a href="link"><img src="docs/Notion.png" width="32%"></a>  
    <a href="link"><img src="https://github.com/user-attachments/assets/4a0b74f4-bf4d-4dc1-93e3-1419d25e7047" width="32%"></a>  
</figure>

</div>
</details>

<br/>

### 🏃‍♀️‍➡️ Issue & Jira 를 통한 트래킹 일정 관리

<details>
<summary>개발일정 관리</summary>
<div markdown="1">

<img src="docs/Issue.png" alt="Alt text" width="980" height="610"/>

<p align="center">
    <img src="https://github.com/user-attachments/assets/589e3eee-997d-48a8-adb9-18fb3dd9045a" align="center" width="32%">  
    <img src="https://github.com/user-attachments/assets/11a9b040-a855-4533-bbe8-d3cc63240b01" align="center" width="32%">  
</p>

</div>
</details>

<br/>

### 🤖 Discord을 활용한 소통 및 PR 알림 봇

<details>
<summary>소통 및 PR 알림 확인</summary>
<div markdown="1">

![img_1.png](docs/img_1.png)
<img src="docs/img_2.png" alt="Alt text" width="430" height="600"/>

</div>
</details>

<br/>

## 프로젝트 환경

- Spring boot 3.3.x
- Gradle 8.8
- JDK 17
- MySQL 8.0
- Redis 6.0

### 요구사항 정의서 정리

![img.png](docs/요구사항.png)

### API 명세서

![img.png](docs/img.png)

### ERD

![img.png](docs/ERD.png)

## 트러블 슈팅
<details>
  <summary> <strong> 💥 1. JWT 토큰 시크릿 키 보안 오류 </strong> </summary>
  
- 작성자: 김은정
### (1) 문제 상황
- JWT 토큰 발행을 위한 Secret Key 설정 중 발생

### (2) 발생한 문제(에러)
>해당 에러 발생 : `io.jsonwebtoken.security.WeakKeyException`

### (3) 원인
- 추정되는 원인
  - 오류 제목을 보고 보안이 약한 Secret Key를 설정했기 때문이라고 생각
- 실제 원인
  - Secret Key를 256bit 미만으로 설정했기 때문에 발생

### (4) 해결방법
- Secret Key를 좀 더 길게 설정

### (5) 참고자료
- [Spring Security - io.jsonwebtoken.security.WeakKeyException 원인과 해결 방법](https://green-bin.tistory.com/49)

</details>

<br>

<details>
<summary> <strong> 🤔 2. MultiValueMap으로 인한 415 UNSUPPORTED_MEDIA_TYPE 오류 </strong></summary>

- 작성자: 김은정
 ### (1) 문제 상황
> 로그인 통합 테스트 실행 중 `username`과 `password`를 `MultiValueMap`에 저장해 컨트롤러로 로그인을 요청하던 중 발생

### (2) 발생한 문제(에러)
  - ⓐ 최초 `💥NullPointerException`이 발생하면서 <u>"가입되지 않은 아이디"</u>라는 사용자 정의 에러 메시지가 전달됨 <p></p>
- ⓑ `@BeforeEach` 애노테이션을 이용해 로그인 테스트 직전에 회원가입을 실행했기 때문에 전달된
  
  ResponseEntity의 HttpStatus를 확인해봤는데, `💥415 UNSUPPORTED_MEDIA_TYPE` 오류가 발생

### (3) 원인
- 추정되는 원인
  - `MultiValueMap`에 담긴 데이터가 파라미터와 바인딩되지 않아서 발생하는 오류라고 생각

- 실제 원인 
  - `MultiValueMap`은 기본적으로 데이터를 `application/x-www-form-urlencoded` 타입으로 받아서 인코딩 후 JSON으로 변환한다. 
  - 그런데, JSON 형식의 데이터를 보내서 415 UNSUPPORTED_MEDIA_TYPE 에러가 발생한 것 같다.

### (4) 해결방법
> `MultiValueMap` 대신 파라미터와 같은 객체를 사용

### (5) 참고자료

- [x-www-form-urlencoded 타입이란: multipart/form-data와의 차이점](https://wildeveloperetrain.tistory.com/304)
- [[Spring boot] Post 요청, Content-Type](https://velog.io/@hyerin_story/Spring-boot-Post-요청-Content-Type)

</details>

<br>

<details>
  <summary> <strong> 💦 3. Querydsl 설정 관련 이슈 </strong> </summary>

  - 작성자: 유리빛나, 오예령
### (1) 문제 상황
> 게시물 검색, 목록 조회, 통계 API에 활용하기 위해 Querydsl 설정 중 버전 관련하여 문제 발생

### (2) 발생한 환경, 프로그램
>- Spring Boot 3.3.x
- Gradle 8.8
- JDK 17
- IntelliJ

### (3) 발생한 문제(에러)
- 이전에 사용해본 Querydsl 설정 방법으로 build.gradle에 추가하였으나,
  Qclass를 생성하는 과정에서 의존성에서 `compileQuerydsl`을 실행하지 못하면서 빌드가 안되는 현상❌ 발생 <p></p>
- 발생한 오류를 검색해보니 `Spring Boot 2.xx 버전과 3.xx 버전 차이`로 설정하는 방식이 변경되었다는 것을 알게 되었습니다.
  
  - 저희 프로젝트의 환경에 맞는 환경설정을 검색해 <u>의존성 주입 및 저장 경로를 추가해보았지만</u> 오류가 바뀌지 않았음

### (4) 원인
- 1차 원인버전에 따른 dependency 변경  
2차 원인: IntelliJ의 `Setting > Build, Execution, Deployment > Gradle` 설정  
경로 설정에 따라 Qclass들이 생성되는 위치가 달라지며, 이는 Gradle 설정의 영향을 받습니다.

- a. `$buildDir/generated/querydsl`
  - 프로젝트 최상단의 build 폴더 > generated 폴더 > querydsl
- b. `src/main/generated`
  - 프로젝트 src 폴더 > main 폴더 > generated

### (5) 해결 방법
- 저희 프로젝트의 버전에 맞는 설정을 찾기 위해 다양한 시도를 해보았습니다.

>1. Commit > Unversioned Files에 있는 Q클래스 파일 삭제
2. `gradle > build > clean`
3. `gradle > build > build`
4. 재부팅 시도

> **다른 방법**
1. `gradle > build > clean` 실행 후 `build` 실행해보기
2. `Settings > Build > Build Tools > Gradle > IntelliJ`로 변경해보기
3. `Settings > Build > Compiler > Annotation Processors` 활성화해보기

</details>

<br>

<details>
  <summary> <strong> 🔥 4. Enum의 유효성 검사 및 예외처리 </strong></summary>

- 작성자: 김유현
### (1) 문제 상황
> 쿼리 파라미터가 길고 복잡하여 DTO로 생성하였습니다.  
추가로 잘못된 Enum 값이 들어올 경우, 해당 타입에 따라서 원하는 에러 코드를 던지고 싶었습니다.

### (2) 발생한 문제(에러)
> 어떤 Enum값이든 상관없이 유효하지 않은 타입이 왔을 때 `BindException`을 던져서 언제 어느 타입이 유효하지 않은지 메시지를 정확히 주기 어려웠습니다.

### (3) 원인
> DTO에서 들어올 때, `@ModelAttribute`에서 타입이 맞지 않으면 `BindException`을 내기 때문이었습니다.

### (4) 최종 해결
> Global Exception Handler에서 파라미터명에 따라서 에러 코드를 매핑해주었습니다.  

### (5) 해결 방법

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
### (6) 개선 방안
> - 컨트롤러나 메소드 명에 따라서, 의도한 에러코드를 낼 수 있도록 수정하거나,
커스텀 어노테이션을 만들어 Enum을 유효성 검사할 수 있도록 해야겠다.

### (7) 참고 자료
- [Enum 유효성 검사하기](https://cchoimin.tistory.com/entry/Enum-유효성-검사하기)
- [Enum Validation](https://tommykim.tistory.com/20)

</details>

<br>

<details>
  <summary><strong> 😅 5. 목(Mock) 객체 사용 및 테스트 코드 작성 이해 부족 </strong></summary>

- 작성자: 배서진
### (1) 문제 상황

>```java
1. JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
2. jwtTokenProvider.createToken(); 의 차이를 이해하지 못하여 테스트 코드 작성 시 시간이 많이 소요되었습니다.
```

### (2) 발생한 문제(에러)
>- `extractEmail`이나 `extractAllClaims` 등의 모킹이 제대로 설정되지 않음
- 이메일을 추출하는 과정에서 `null` 반환


### 3. 원인
- 추정되는 원인
> - 실제 로그인한 유저의 token을 받아와야 한다고 생각
> - 어노테이션의 설정 오류라고 생각
  - `@Mock`, `@MockBean`, `@AutoConfigureMockMvc`
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

### (4) 해결 방법
- `JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);` 으로 사용

### (5) 참고 자료
- [mock() vs @Mock vs @MockBean 이제 그만 헷갈리자!](https://simgee.tistory.com/58)
- **실제 JWT 토큰 생성이 필요 없는 이유**
  - 테스트에서 JWT 토큰이 어떻게 생성되는지 자체는 중요한 부분이 아닙니다.
  - 단지 토큰에서 올바른 이메일이 추출되고, 그 결과가 이메일 전송 로직으로 이어지는지, 그리고 예외가 발생할 때 올바르게 처리되는지에만 집중하면 됩니다.

- **목 객체(Mock Object)**
  - 단위 테스트에서 사용되는 가짜 객체

- **스터빙(stubbing)**
  - 목 객체가 어떤 메소드를 호출할 때, 실제 메소드의 동작을 대신하여 미리 정해진 값을 반환하거나, 특정 동작을 수행하도록 하는 과정

</details>

