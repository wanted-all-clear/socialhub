package com.allclear.socialhub.user.service;

import com.allclear.socialhub.user.repository.UserEmailVerificationRepository;
import com.allclear.socialhub.user.type.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserEmailVerificationRepository emailVerificationRepository;

    private final long expire_period = 1000L * 60L * 10; // 10분

    /**
     * 사용자에게 인증 이메일을 비동기적으로 전송합니다.
     *
     * @param token     JWT token
     * @param emailType 이메일 템플릿의 유형 EmailType enum
     */
    @Async
    @Override
    public void sendEmail(String token, EmailType emailType) {
        // 1. 인증 번호 생성
        String authCode = createCode();

        //TODO: JwtUils 클래스 생성되면, 해당 클래스에 메서드 추가 예정
        /* JWT 토큰에서 이메일 추출
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
        String email = claims.get("email", String.class);
        */
        String email = "wpdls879@gmail.com"; //TODO: jwt 기능 구현 완료 후 삭제

        // 2. 인증 번호를 Redis에 저장 (유효시간 10분)
        emailVerificationRepository.saveVerificationToken(email, authCode, expire_period);

        // 3. 이메일 생성 및 전송
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Socialhub 이메일 인증 코드를 발송합니다.");
            mimeMessageHelper.setText(setContext(authCode, emailType), true); // 메일 본문 내용, HTML 여부

            javaMailSender.send(mimeMessage);
            log.info("인증 코드가 {}에 전송되었습니다.", email);
        } catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 8자리의 랜덤 인증 코드를 생성합니다.
     * 작성자: 배서진
     *
     * @return 생성된 8자리 인증 코드 문자열
     */
    private String createCode() {

        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97)); // 소문자 추가 ('a' ~ 'z')
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));  // 대문자 추가 ('A' ~ 'Z')
                    break;
                default:
                    key.append(random.nextInt(10)); // 숫자 추가 ('0' ~ '9')
            }
        }
        return key.toString(); // 8글자
    }

    /**
     * Thymeleaf 템플릿을 사용하여 이메일 본문 내용을 생성합니다.
     *
     * @param code 이메일에 포함될 인증 코드
     * @param type 사용할 이메일 템플릿의 유형
     * @return 생성된 이메일 본문 HTML 문자열
     */
    public String setContext(String code, EmailType type) {

        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type.getTemplateName(), context);
    }

    // 인증 코드 조회
    public String getVerificationToken(String email) {

        return emailVerificationRepository.getVerificationToken(email);
    }

    // 인증 코드 삭제
    public void deleteVerificationToken(String email) {

        emailVerificationRepository.deleteVerificationToken(email);
    }


}
