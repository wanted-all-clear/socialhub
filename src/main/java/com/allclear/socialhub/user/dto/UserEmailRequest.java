package com.allclear.socialhub.user.dto;

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

    @NotBlank(message = "인증 코드는 필수 항목입니다.")
    private String authCode;

}
