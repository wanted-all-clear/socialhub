package com.allclear.socialhub.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("STATUS_ACTIVE"),
    BLOCKED("STATUS_BLOCKED"),
    WITHDRAW("STATUS_WITHDRAW");

    private final String state;
}
