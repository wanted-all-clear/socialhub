package com.allclear.socialhub.post.common.hashtag.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "hashtag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String content;
}
