package com.allclear.socialhub.post.common.hashtag.repository;

import com.allclear.socialhub.post.common.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Query("SELECT ph.post.id " +
            "FROM HashTag AS h " +
            "INNER JOIN PostHashTag AS ph on h.id = ph.hashTag.id " +
            "where h.content = :hashtag")
    List<Long> getPostByHashtag(@Param("hashtag") String hashtag);

}
