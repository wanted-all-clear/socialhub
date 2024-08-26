package com.allclear.socialhub.post.common.view.repository;

import com.allclear.socialhub.post.common.response.StatisticQueryResponse;
import com.allclear.socialhub.post.common.view.domain.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    @Query("SELECT DATE_FORMAT(pv.createdAt, :dateFormatPattern) AS time, count(*) AS value " +
            "FROM PostView AS pv " +
            "WHERE pv.post.id in :postIds " +
            "AND DATE(pv.createdAt) BETWEEN :start AND :end " +
            "GROUP BY DATE_FORMAT(pv.createdAt, :dateFormatPattern) " +
            "ORDER BY time ASC")
    List<StatisticQueryResponse> findStatisticByPostIds(
            @Param("postIds") List<Long> postIds,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("dateFormatPattern") String dateFormatPattern);

    void deleteAllByPostId(Long postId);

}
