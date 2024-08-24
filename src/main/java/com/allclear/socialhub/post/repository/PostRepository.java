package com.allclear.socialhub.post.repository;

import com.allclear.socialhub.post.common.response.StatisticQueryResponse;
import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.repository.querydsl.PostRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuerydsl {

    @Query("SELECT DATE_FORMAT(p.createdAt, :dateFormatPattern) AS time, count(*) AS value " +
            "FROM Post AS p " +
            "WHERE p.id in :postIds " +
            "AND DATE(p.createdAt) BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(p.createdAt, :dateFormatPattern) " +
            "ORDER BY time ASC")
    List<StatisticQueryResponse> findStatisticByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("dateFormatPattern") String dateFormatPattern);h
}
