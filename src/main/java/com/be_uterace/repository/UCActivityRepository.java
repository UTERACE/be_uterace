package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.UserClubActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UCActivityRepository extends JpaRepository<UserClubActivity,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserClubActivity uca SET uca.status = '0', " +
            "uca.reason = :reason WHERE uca.club.clubId = :clubId and uca.run.runId = :runId")
    int updateStatusAndReasonByClubIdAndRunId(String reason, Long clubId, Long runId);


}
