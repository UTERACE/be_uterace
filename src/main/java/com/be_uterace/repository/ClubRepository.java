package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.projection.ClubRankingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
