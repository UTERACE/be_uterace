package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Run;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RunRepository extends JpaRepository<Run,Long> {
    boolean existsByStravaRunId(Long aLong);
    List<Run> findAllByUser_UserId(Long userId);

    @Query("SELECT r FROM Run r WHERE " +
            "(:searchName IS NULL OR unaccent(LOWER(r.name)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))) " +
            "AND r.createdAt >= :thresholdDateTime " +
            "AND r.user.userId = :userId " +
            "ORDER BY r.runId DESC")
    Page<Run> findRunsByDateTimeAndName(
            @Param("thresholdDateTime") Timestamp thresholdDateTime,
            @Param("searchName") String searchName,
            @Param("userId") Long userId,
            Pageable pageable
    );

}
