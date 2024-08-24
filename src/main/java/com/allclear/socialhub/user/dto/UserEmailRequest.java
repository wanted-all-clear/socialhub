package com.allclear.socialhub.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailRequest {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식이 아닙니다. 유효한 이메일 주소를 입력해주세요.")
    private String email;
    
    @NotBlank(message = "인증 코드는 필수 항목입니다.")
    private String authCode;

}
