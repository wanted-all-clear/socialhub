package com.allclear.socialhub.post.common.hashtag.domain;

import com.allclear.socialhub.post.domain.Post;
import jakarta.persistence.*;

@Entity
@Table(name = "post_hashtag")
public class PostHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private HashTag hashTag;
}
