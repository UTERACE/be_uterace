package com.be_uterace.repository;

import com.be_uterace.entity.UserEventActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UEActivityRepository extends JpaRepository<UserEventActivity,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserEventActivity uea SET uea.status = '0', " +
            "uea.reason = :reason WHERE uea.event.eventId = :eventId and uea.run.runId = :runId")
    void updateStatusAndReasonByEventIdAndRunId(String reason, Long eventId, Long runId);
}
