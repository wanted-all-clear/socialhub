package com.allclear.socialhub.post.repository;

import com.allclear.socialhub.post.common.response.StatisticResponse;
import com.allclear.socialhub.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DATE_FORMAT(p.createdAt, '%Y-%m-%d') AS time, count(*) AS value " +
            "FROM Post AS p " +
            "WHERE p.id in :postIds " +
            "AND DATE(p.createdAt) BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(p.createdAt, '%Y-%m-%d') " +
            "ORDER BY time ASC")
    List<StatisticResponse> findDailyStatisticByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

}
