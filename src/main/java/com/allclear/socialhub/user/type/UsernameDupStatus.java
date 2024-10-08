package com.allclear.socialhub.user.type;

import lombok.Getter;

@Getter
public enum UsernameDupStatus {
	USERNAME_AVAILABLE("해당 계정은 사용이 가능합니다.");

	private final String message;

	UsernameDupStatus(String message) {
		this.message = message;
	}
}
