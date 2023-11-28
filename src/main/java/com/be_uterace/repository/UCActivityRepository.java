package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Run;
import com.be_uterace.entity.UserClubActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface UCActivityRepository extends JpaRepository<UserClubActivity,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserClubActivity uca SET uca.status = '0', " +
            "uca.reason = :reason WHERE uca.club.clubId = :clubId and uca.id = :id")
    int updateStatusAndReasonByClubIdAndRunId(String reason, Long clubId, Long id);


    @Query("SELECT uca FROM UserClubActivity uca WHERE " +
            "(:searchName IS NULL OR unaccent(LOWER(uca.name)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))) " +
            "AND uca.createdAt >= :thresholdDateTime " +
            "AND uca.club.clubId = :clubId " +
            "ORDER BY uca.id DESC")
    Page<UserClubActivity> findActivityByDateTimeAndName(
            @Param("thresholdDateTime") Timestamp thresholdDateTime,
            @Param("searchName") String searchName,
            @Param("clubId") Integer clubId,
            Pageable pageable
    );



}
