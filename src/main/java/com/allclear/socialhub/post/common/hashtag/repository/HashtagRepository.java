package com.allclear.socialhub.post.common.hashtag.repository;

import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByContent(String content);

    @Query("SELECT ph.post.id " +
            "FROM Hashtag AS h " +
            "INNER JOIN PostHashtag AS ph on h.id = ph.hashtag.id " +
            "where h.content = :hashtag")
    List<Long> getPostByHashtag(@Param("hashtag") String hashtag);

}
