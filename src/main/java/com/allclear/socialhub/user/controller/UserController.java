package com.allclear.socialhub.user.controller;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.user.dto.UserEmailRequest;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.service.EmailService;
import com.allclear.socialhub.user.service.UserService;
import com.allclear.socialhub.user.type.EmailType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.allclear.socialhub.user.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.allclear.socialhub.common.provider.JwtTokenProvider;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {

    private final EmailService emailService;
    private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "이메일 인증 코드 전송", description = "사용자의 이메일로 인증 코드를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드가 이메일로 전송됨",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패",
                    content = @Content)
    })
    @PostMapping("/email-code")
    public ResponseEntity<String> sendEmailVerification() throws MessagingException {
        // TODO: jwt 반영되면 추가 예정
        // @RequestHeader("Authorization") String token
        //    String jwtToken = token.substring(7);

        emailService.sendEmail("wpdls879@gmail.com", EmailType.VERIFICATION); //TODO
        return ResponseEntity.status(HttpStatus.OK).body("이메일로 인증 코드가 전송되었습니다.");
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "사용자가 입력한 인증 코드를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "인증 코드가 일치하지 않거나 만료됨",
                    content = @Content)
    })
    @PostMapping("/email-verify")
    public ResponseEntity<String> verifyEmailCode(@Valid @RequestBody UserEmailRequest request) { // TODO: JWT 기능 구현 완료 후 email은 token에서 처리

        String storedCode = emailService.getVerificationToken(request.getEmail());

        if (userService.verifyUser(storedCode, request.getAuthCode(), request.getEmail())) {
            return ResponseEntity.status(HttpStatus.OK).body("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않거나 만료되었습니다.");
        }
    }

    @Operation(summary = "회원 가입", description = "사용자가 회원 가입을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<String> joinUser(@Valid @RequestBody UserJoinRequest request) {
		try {
			userService.joinUser(request);
			return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
		} catch (CustomException e) {
			throw e;
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> userLogin(@RequestBody UserLoginRequest request) {
		User user = userService.userLogin(request);
		String jwtToken = jwtTokenProvider.createToken(user);

		return ResponseEntity.ok().header("AUTHORIZATION", jwtToken).body("로그인이 완료되었습니다.");
	}

}
