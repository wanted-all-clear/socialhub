package com.allclear.socialhub.post.common.view.repository;

import com.allclear.socialhub.post.common.response.StatisticResponse;
import com.allclear.socialhub.post.common.view.domain.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    @Query("SELECT DATE_FORMAT(pv.createdAt, '%Y-%m-%d') AS time, count(*) AS value " +
            "FROM PostView AS pv " +
            "WHERE pv.post.id in :postIds " +
            "AND DATE(pv.createdAt) BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(pv.createdAt, '%Y-%m-%d') " +
            "ORDER BY time ASC")
    List<StatisticResponse> findStatisticDtoByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

}
