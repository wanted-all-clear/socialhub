package com.allclear.socialhub.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCertifyStatus {
    UNAUTHENTICATED("UNAUTHENTICATED"),
    AUTHENTICATED("AUTHENTICATED");

    private final String certifyStatus;
}
