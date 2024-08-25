package com.allclear.socialhub.user.controller;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.user.dto.UserEmailRequest;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.service.EmailServiceImpl;
import com.allclear.socialhub.user.service.UserServiceImpl;
import com.allclear.socialhub.user.type.EmailType;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final EmailServiceImpl emailService;
    private final UserServiceImpl userService;

    
    @PostMapping("/email-code")
    public ResponseEntity<String> sendEmailVerification() throws MessagingException {
        // @RequestHeader("Authorization") String token
        //    String jwtToken = token.substring(7);

        emailService.sendEmail("wpdls879@gmail.com", EmailType.VERIFICATION);
        return new ResponseEntity<>("이메일로 인증 코드가 전송되었습니다.", HttpStatus.OK);
    }

    /**
     * 인증 코드 확인을 처리합니다.
     * 작성자: 배서진
     *
     * @param request 인증 코드와 이메일 정보를 포함한 요청 데이터
     * @return 인증 결과
     */
    @PostMapping("/email-verify")
    public ResponseEntity<String> verifyEmailCode(@Valid @RequestBody UserEmailRequest request) { // TODO: JWT 기능 구현 완료 후 email은 token에서 처리

        String storedCode = emailService.getVerificationToken(request.getEmail());

        if (storedCode != null && storedCode.equals(request.getAuthCode())) {
            emailService.deleteVerificationToken(request.getEmail());
            return new ResponseEntity<>("이메일 인증이 완료되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("인증 코드가 일치하지 않거나 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ResponseEntity<String> joinUser(@Valid @RequestBody UserJoinRequest request) {

        try {
            userService.joinUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        } catch (CustomException e) {
            throw e;
        }
    }


}
