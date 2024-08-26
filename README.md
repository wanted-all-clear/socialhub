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
| 담당자       | 담당 업무                                                 |
|--------------|----------------------------------------------------------|
| [오예령(팀장)](https://github.com/ohyeryung) | 게시물 기능 구현 (등록, 수정, 삭제, 검색)                  |
| [유리빛나](https://github.com/ryuneng)     | 게시물 기능 구현 (목록 조회, 상세 조회, 좋아요, 공유)       |
| [김유현](https://github.com/youhyeoneee)       | 통계 기능 구현 (서비스 및 컨트롤러, 단위 테스트)           |
| [김은정](https://github.com/fkznsha23)| 사용자 기능 구현 (로그인, 계정 중복 확인)                  |
| [김효진](https://github.com/hyojin52)       | 통계 기능 구현 (서비스 및 레포지토리, 스웨거)              |
| [배서진](https://github.com/bsjin1122)       | 사용자 기능 구현 (회원가입, 이메일 인증 요청&검증)         |


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

| Stack                                                                | Version            |
|:----------------------------------------------------------------------:|:-----------------:|
| ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)  | Spring Boot 3.3.x |
| ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)    | Gradle 8.8       |
| ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)    | JDK 17           |
| ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)       | MySQL 8.0        |
| ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)    | Redis 6.0        |

### 요구사항 정의서 정리

| 도메인 | 기능명               | Controller & Service method   | 담당자        |
|--------|----------------------|-------------------------|---------------|
| 사용자 | 사용자 회원가입        | joinUser                | 배서진        |
| 사용자 | 사용자 로그인         | loginUser               | 김은정        |
| 사용자 | 사용자 이메일 인증 요청 | sendEmailVerification    | 배서진        |
| 사용자 | 사용자 이메일 인증 검증 | verifyEmailCode         | 배서진        |
| 사용자 | 사용자 계정 중복 확인  | userDuplicateCheck      | 김은정        |
| 사용자 | 사용자 회원정보 수정   | updateUserInfo          | 배서진        |
| 게시물 | 게시물 검색 필터링     | searchPosts             | 오예령        |
| 게시물 | 게시물 목록 조회      | getPosts                | 유리빛나       |
| 게시물 | 게시물 상세 조회      | getPostDetail           | 유리빛나       |
| 게시물 | 게시물 등록           | createPost              | 오예령        |
| 게시물 | 게시물 삭제           | deletePost              | 오예령        |
| 게시물 | 게시물 수정           | updatePost              | 오예령        |
| 게시물 | 게시물 좋아요         | likePost                | 유리빛나       |
| 게시물 | 게시물 공유           | sharePost               | 유리빛나       |
| 통계   | 통계                  | getStatistics           | 김유현, 김효진 |

### API 명세서

| 도메인 | 기능명               | Http Method | API Path                       | 인증 | 담당자        |
|--------|----------------------|-------------|--------------------------------|------|---------------|
| 사용자 | 사용자 회원가입        | POST        | /api/users                     | X    | 배서진        |
| 사용자 | 사용자 가입 승인       | POST        | /api/users/allow               | O    | 배서진        |
| 사용자 | 사용자 로그인         | POST        | /api/users/login               | X    | 김은정        |
| 사용자 | 사용자 이메일 인증 요청 | POST        | /api/users/email-verification  | O    | 배서진        |
| 사용자 | 사용자 이메일 인증 검증 | POST        | /api/users/email-verification  | O    | 배서진        |
| 사용자 | 사용자 계정 중복 확인  | POST        | /api/users/duplicate-check     | X    | 김은정        |
| 사용자 | 사용자 회원정보 수정   | PATCH       | /api/users            | O    | 배서진        |
| 게시물 | 게시물 검색 필터링     | GET         | /api/posts/search              | O    | 오예령        |
| 게시물 | 게시물 목록 조회      | GET         | /api/posts                     | O    | 유리빛나       |
| 게시물 | 게시물 상세 조회      | GET         | /api/posts/{postId}            | O    | 유리빛나       |
| 게시물 | 게시물 등록           | POST        | /api/posts                     | O    | 오예령        |
| 게시물 | 게시물 삭제           | DELETE      | /api/posts/{postId}            | O    | 오예령        |
| 게시물 | 게시물 수정           | PUT         | /api/posts/{postId}            | O    | 오예령        |
| 게시물 | 게시물 좋아요         | POST        | /api/posts/{postId}/like       | O    | 유리빛나       |
| 게시물 | 게시물 공유           | POST        | /api/posts/{postId}/share      | O    | 유리빛나       |
| 통계   | 통계                  | GET         | /api/statistics                | O    | 김유현, 김효진 |



### ERD

![img.png](docs/ERD.png)

## 트러블 슈팅
- [💥 **1. JWT 토큰 시크릿 키 보안 오류**](https://github.com/wanted-all-clear/socialhub/blob/docs/%23ALL-94-docs-trouble-shooting-1/readme/trouble/JWT_%ED%86%A0%ED%81%B0_%EC%8B%9C%ED%81%AC%EB%A6%BF_%ED%82%A4_%EB%B3%B4%EC%95%88_%EC%98%A4%EB%A5%98.md)  

<p></p>

- [🤔 **2. MultiValueMap으로 인한 415 UNSUPPORTED_MEDIA_TYPE 오류**](https://github.com/wanted-all-clear/socialhub/blob/docs/%23ALL-94-docs-trouble-shooting-1/readme/trouble/MultiValueMap_415_ERROR.md)

<p></p>

- [💦 **3. Querydsl 설정 관련 이슈**](https://github.com/wanted-all-clear/socialhub/blob/docs/%23ALL-94-docs-trouble-shooting-1/readme/trouble/Querydsl_%EC%84%A4%EC%A0%95_%EA%B4%80%EB%A0%A8_%EC%9D%B4%EC%8A%88.md)

<p></p>

- [🔥 **4. Enum의 유효성 검사 및 예외처리**](https://github.com/wanted-all-clear/socialhub/blob/docs/%23ALL-94-docs-trouble-shooting-1/readme/trouble/Enum%EC%9D%98_%EC%9C%A0%ED%9A%A8%EC%84%B1_%EA%B2%80%EC%82%AC_%EB%B0%8F_%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC.md)

<p></p>

- [😅 **5. 목(Mock) 객체 사용 및 테스트 코드 작성에 어려움**](https://github.com/wanted-all-clear/socialhub/blob/docs/%23ALL-94-docs-trouble-shooting-1/readme/trouble/%EB%AA%A9(Mock)_%EA%B0%9D%EC%B2%B4_%EC%82%AC%EC%9A%A9_%EB%B0%8F_%ED%85%8C%EC%8A%A4%ED%8A%B8_%EC%BD%94%EB%93%9C_%EC%9E%91%EC%84%B1_%EC%9D%B4%ED%95%B4_%EB%B6%80%EC%A1%B1.md)

