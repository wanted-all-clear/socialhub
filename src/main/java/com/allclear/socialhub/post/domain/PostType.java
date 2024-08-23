package com.allclear.socialhub.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {

    INSTAGRAM("인스타그램"),
    TWITTER("트위터"),
    FACEBOOK("페이스북"),
    THREADS("스레드");

    private final String type;

}
