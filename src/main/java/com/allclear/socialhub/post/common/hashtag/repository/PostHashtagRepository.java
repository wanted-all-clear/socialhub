package com.allclear.socialhub.post.common.hashtag.repository;

import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    List<PostHashtag> findAllByPostId(Long postId);

}
