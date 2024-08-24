package com.allclear.socialhub.user.controller;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.user.dto.UserInfoUpdateRequest;
import com.allclear.socialhub.user.dto.UserInfoUpdateResponse;
import com.allclear.socialhub.user.dto.UserJoinRequest;
import com.allclear.socialhub.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("")
    public ResponseEntity<String> joinUser(@Valid @RequestBody UserJoinRequest request) {

        try {
            userService.joinUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        } catch (CustomException e) {
            throw e;
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserInfoUpdateResponse> updateUserInfo(@Valid @RequestBody UserInfoUpdateRequest request) {
        /* TODO: Parameter 추가 예정 - @RequestHeader("Authorization") String token

           // 1. JWT 토큰에서 "Bearer " 접두어 제거
           String jwtToken = token.substring(7);

           // 2. 토큰에서 이메일 추출 및 예외 처리
           String email;
           try {
               email = jwtService.getEmailFromToken(jwtToken);
           } catch (ExpiredJwtException e) {
               throw new CustomException(ErrorCode.TOKEN_EXPIRED);
           } catch (JwtException | IllegalArgumentException e) {
               throw new CustomException(ErrorCode.TOKEN_INVALID);
           }
         */
        String email = "wpdls879@gmail.com";
        
        UserInfoUpdateResponse response = userService.updateUserInfo(request, email);

        return ResponseEntity.status(200).body(response);
    }


}
