package com.allclear.socialhub.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    WITHDRAW("WITHDRAW");

    private final String state;
}
