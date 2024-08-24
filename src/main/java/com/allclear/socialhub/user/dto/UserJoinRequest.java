package com.allclear.socialhub.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserJoinRequest {

    @NotBlank(message = "사용자명은 필수 항목입니다.")
    @Size(min = 3, max = 20, message = "사용자명은 3자 이상, 20자 이하이어야 합니다.")
    String username;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식이 아닙니다. 유효한 이메일 주소를 입력해주세요.")
    String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상, 20자 이하이어야 합니다.")
    String password;

}
