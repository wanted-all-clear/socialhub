package com.allclear.socialhub.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserJoinRequest {

    String username;

    @Email(message = "이메일 형식이 아닙니다")
    String email;

    String password;

}
