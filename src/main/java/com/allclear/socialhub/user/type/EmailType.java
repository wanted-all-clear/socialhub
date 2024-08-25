package com.allclear.socialhub.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 이메일 전송에 사용되는 유형들을 담고 있는 ENUM입니다.
 * 작성자: 배서진
 * <p>
 * VERIFICATION: 인증 코드 이메일을 보낼 때
 * WELCOME: 회원가입 환영 이메일을 보낼 때
 * PASSWORD_RESET: 비밀번호 재설정 이메일을 보낼 때
 */
@Getter
@RequiredArgsConstructor
public enum EmailType {
    VERIFICATION("USER_VERIFICATION"),
    WELCOME("WELCOME"),
    PASSWORD_RESET("PASSWORD_RESET");

    private final String templateName;
}
