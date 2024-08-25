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
     * 이메일은 인증 코드를 포함하며, 특정 템플릿을 사용하여 생성됩니다.
     * 작성자: 배서진
     *
     * @param email     이메일 주소
     * @param emailType 이메일 템플릿의 유형 EmailType enum
     * @throws MessagingException 이메일 전송 중 발생할 수 있는 예외
     */
    @Async
    @Override
    public void sendEmail(String email, EmailType emailType) throws MessagingException {

        log.info("이메일 인증 코드 전송 시작");

        // 1) 인증 번호 생성
        String authCode = createAuthCode();

        // JWT에서 이메일 추출 (TODO: 현재는 하드코딩된 이메일 사용)
        email = "wpdls879@gmail.com";

        // 2) 인증 번호를 Redis에 저장
        saveAuthCodeToRedis(email, authCode);

        // 3) 이메일 콘텐츠 생성
        String content = generateEmailContent(authCode, emailType);

        // 4) 이메일 메시지 생성
        MimeMessage mimeMessage = createMimeMessage(email, content);

        // 5) 이메일 전송
        sendMimeMessage(mimeMessage);

        log.info("인증 코드가 {}에 전송되었습니다.", email);
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
     * 작성자: 배서진
     *
     * @param code 이메일에 포함될 인증 코드
     * @param type 사용할 이메일 템플릿의 유형
     * @return 생성된 이메일 본문 HTML 문자열
     */
    public String setContext(String code, EmailType type) {

        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("verification", context);
    }

    /**
     * 저장된 인증 코드를 조회합니다.
     *
     * @param email 인증 코드를 조회할 이메일 주소
     * @return 저장된 인증 코드 문자열
     */
    public String getVerificationToken(String email) {

        return emailVerificationRepository.getVerificationToken(email);
    }

    /**
     * 인증 코드 정보를 삭제합니다.
     *
     * @param email 인증 코드를 삭제할 이메일 주소
     */
    public void deleteVerificationToken(String email) {

        emailVerificationRepository.deleteVerificationToken(email);
    }

    /**
     * 1) 인증 코드를 생성하고 로그에 출력합니다.
     * 작성자: 배서진
     *
     * @return 생성된 인증 코드
     */
    private String createAuthCode() {

        String authCode = createCode(); // createCode() 메서드를 사용
        log.debug("생성된 인증 코드: {}", authCode);
        return authCode;
    }

    /**
     * 2) 인증 코드를 Redis에 저장합니다.
     * 작성자: 배서진
     *
     * @param email    인증 코드를 저장할 이메일 주소
     * @param authCode 저장할 인증 코드
     */
    private void saveAuthCodeToRedis(String email, String authCode) {

        emailVerificationRepository.saveVerificationToken(email, authCode, expire_period);
        log.info("인증 코드가 Redis에 저장되었습니다. 이메일: {}, 유효시간: {}분", email, expire_period / (1000 * 60));
    }

    /**
     * 3) 이메일 콘텐츠를 생성합니다.
     * 작성자: 배서진
     *
     * @param authCode  이메일에 포함될 인증 코드
     * @param emailType 사용할 이메일 템플릿의 유형
     * @return 생성된 이메일 콘텐츠
     */
    private String generateEmailContent(String authCode, EmailType emailType) {

        String content = setContext(authCode, emailType);
        log.debug("생성된 이메일 콘텐츠: {}", content);
        return content;
    }

    /**
     * 4) MimeMessage 객체를 생성합니다.
     * 작성자: 배서진
     *
     * @param email   이메일 수신자 주소
     * @param content 이메일 본문 내용
     * @return 생성된 MimeMessage 객체
     */
    private MimeMessage createMimeMessage(String email, String content) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("SocialHub 이메일 인증 코드를 발송합니다.");
            mimeMessageHelper.setText(content, true); // 메일 본문 내용, HTML 여부

            return mimeMessage;
        } catch (MessagingException e) {
            log.error("이메일 메시지 생성 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("이메일 메시지 생성 중 오류 발생", e);
        }
    }

    /**
     * 5) MimeMessage 객체를 통해 이메일을 전송합니다.
     * 작성자: 배서진
     *
     * @param mimeMessage 전송할 MimeMessage 객체
     * @throws MessagingException 이메일 전송 중 오류가 발생할 경우
     */
    private void sendMimeMessage(MimeMessage mimeMessage) throws MessagingException {

        javaMailSender.send(mimeMessage);
    }


}
