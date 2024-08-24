package com.allclear.socialhub.post.common.hashtag.repository;

import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByContent(String content);

}
