package com.allclear.socialhub.post.common.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allclear.socialhub.post.common.hashtag.domain.PostHashtag;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

}
