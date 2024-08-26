package com.allclear.socialhub.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoUpdateResponse {

    private String username;
    private String message;

}
