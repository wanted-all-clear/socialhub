package com.allclear.socialhub.post.common.hashtag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	Optional<Hashtag> findByContent(String content);

}
