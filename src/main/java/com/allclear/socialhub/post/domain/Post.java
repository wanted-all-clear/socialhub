package com.allclear.socialhub.post.domain;

import com.allclear.socialhub.common.domain.Timestamped;
import com.allclear.socialhub.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false)
    private int viewCnt;

    @Column(nullable = false)
    private int likeCnt;

    @Column(nullable = false)
    private int shareCnt;

    public void update(Post updatePost) {

        this.title = updatePost.getTitle();
        this.content = updatePost.getContent();
    }

    public void updateLikeCnt(Post post) {

        this.likeCnt = post.getLikeCnt() + 1;
    }

    public void updateShareCnt(Post post) {

        this.shareCnt = post.getShareCnt() + 1;
    }

    public void updateViewCnt(Post post) {

        this.viewCnt = post.getViewCnt() + 1;
    }

}
