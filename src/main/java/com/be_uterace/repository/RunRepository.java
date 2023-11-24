package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Run;
import com.be_uterace.projection.ActivitySummaryProjection;
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

    @Query(nativeQuery = true, value = "SELECT * FROM get_user_run_stats(:userId)")
    List<Object[]> getUserRunStats(@Param("userId") Long userId);
//    @Query(nativeQuery = true, value = "SELECT * FROM GetRunSummary(:user_id);")
//    List getEmployeeById(@Param("user_id") Long user_id);


    @Query("SELECT COUNT(r) FROM Run r WHERE r.user.userId = :userId")
    int countRunsByUserId(@Param("userId") Long userId);


}
