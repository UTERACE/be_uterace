package com.be_uterace.repository;

import com.be_uterace.entity.Run;
import com.be_uterace.entity.UserEventActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface UEActivityRepository extends JpaRepository<UserEventActivity,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserEventActivity uea SET uea.status = '0', " +
            "uea.reason = :reason WHERE uea.event.eventId = :eventId and uea.id = :runId")
    int updateStatusAndReasonByEventIdAndRunId(String reason, Long eventId, Long runId);


    @Query("SELECT uea FROM UserEventActivity uea WHERE " +
            "(:searchName IS NULL OR unaccent(LOWER(uea.name)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))) " +
            "AND uea.createdAt >= :thresholdDateTime " +
            "AND uea.event.eventId = :eventId " +
            "ORDER BY uea.id DESC")
    Page<UserEventActivity> findRunsByDateTimeAndName(
            @Param("thresholdDateTime") Timestamp thresholdDateTime,
            @Param("searchName") String searchName,
            @Param("eventId") Integer eventId,
            Pageable pageable
    );

    @Modifying
    @Query("delete from UserEventActivity uea where uea.user.userId = :userId")
    void deleteUserEventActivitiesByUserId(Long userId);
}
