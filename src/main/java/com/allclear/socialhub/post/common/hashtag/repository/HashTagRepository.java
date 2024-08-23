package com.allclear.socialhub.post.common.hashtag.repository;

import com.allclear.socialhub.post.common.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
}
