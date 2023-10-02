package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.projection.ClubRankingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {
    @Query("SELECT c.clubId AS clubId, " +
            "c.clubRanking AS clubRanking, " +
            "c.clubName AS clubName, " +
            "c.status AS picturePath, " +
            "c.clubTotalDistance AS clubTotalDistance, " +
            "(SELECT COUNT(uc.user.userId) FROM UserClub uc WHERE uc.club.clubId = c.clubId) AS memberCount, " +
            "(SELECT COUNT(ce.event.eventId) FROM ClubEvent ce WHERE ce.club.clubId = c.clubId) AS eventCount " +
            "FROM Club c " +
            "WHERE c.status = '1' " +
            "ORDER BY c.clubRanking ASC")
    List<ClubRankingProjection> findTop8ClubsWithMemberAndEventCount();

    @Query("SELECT c.clubId AS clubId, " +
            "c.clubName AS clubName, " +
            "c.picturePath, COALESCE(SUM(r.distance), 0) AS clubTotalDistance, " +
            "COALESCE(SUM(uc.user.userId),0) AS memberCount, COALESCE(SUM(r.runId), 0) AS totalActivities, " +
            "ROW_NUMBER() OVER (ORDER BY COALESCE(SUM(r.distance), 0) DESC) AS clubRanking " +
            "FROM Club c " +
            "LEFT JOIN UserClub uc ON c.clubId = uc.user.userId " +
            "LEFT JOIN Run r ON uc.user.userId = r.user.userId " +
            "WHERE (:month = 0 OR EXTRACT(MONTH FROM r.createdAt) = :month) " +
            "AND (:year = 0 OR EXTRACT(YEAR FROM r.createdAt) = :year) " +
            "GROUP BY c.clubId")
    Page<ClubRankingProjection> findScoreboardClub(
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable
    );

}
