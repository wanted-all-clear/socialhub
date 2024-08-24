package com.allclear.socialhub.post.common.hashtag.domain;

import com.allclear.socialhub.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_hashtag")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostHashtagtemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;

}
