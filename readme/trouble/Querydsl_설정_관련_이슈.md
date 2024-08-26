## (1) 문제 상황
> 게시물 검색, 목록 조회, 통계 API에 활용하기 위해 Querydsl 설정 중 버전 관련하여 문제 발생

## (2) 발생한 환경, 프로그램
>- Spring Boot 3.3.x
- Gradle 8.8
- JDK 17
- IntelliJ

## (3) 발생한 문제(에러)
- 이전에 사용해본 Querydsl 설정 방법으로 build.gradle에 추가하였으나,
  Qclass를 생성하는 과정에서 의존성에서 `compileQuerydsl`을 실행하지 못하면서 빌드가 안되는 현상❌ 발생 <p></p>
- 발생한 오류를 검색해보니 `Spring Boot 2.xx 버전과 3.xx 버전 차이`로 설정하는 방식이 변경되었다는 것을 알게 되었습니다.
  
  - 저희 프로젝트의 환경에 맞는 환경설정을 검색해 <u>의존성 주입 및 저장 경로를 추가해보았지만</u> 오류가 바뀌지 않았음

## (4) 원인
- 1차 원인버전에 따른 dependency 변경  
- 2차 원인: IntelliJ의 `Setting > Build, Execution, Deployment > Gradle` 설정  
경로 설정에 따라 Qclass들이 생성되는 위치가 달라지며, 이는 Gradle 설정의 영향을 받습니다.

- a. `$buildDir/generated/querydsl`
  - 프로젝트 최상단의 build 폴더 > generated 폴더 > querydsl
- b. `src/main/generated`
  - 프로젝트 src 폴더 > main 폴더 > generated

## (5) 해결 방법
- 저희 프로젝트의 버전에 맞는 설정을 찾기 위해 다양한 시도를 해보았습니다.

>1. Commit > Unversioned Files에 있는 Q클래스 파일 삭제
2. `gradle > build > clean`
3. `gradle > build > build`
4. 재부팅 시도

> **다른 방법**
1. `gradle > build > clean` 실행 후 `build` 실행해보기
2. `Settings > Build > Build Tools > Gradle > IntelliJ`로 변경해보기
3. `Settings > Build > Compiler > Annotation Processors` 활성화해보기
