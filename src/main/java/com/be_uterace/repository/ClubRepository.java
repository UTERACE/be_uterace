package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.User;
import com.be_uterace.projection.ClubDetailProjection;
import com.be_uterace.projection.ClubProjection;
import com.be_uterace.projection.ClubRankingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Integer>, JpaSpecificationExecutor<Club> {
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

    List<Club> findTop6ClubsByOutstanding(String outstanding);

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

    @Query("SELECT c.clubId AS clubId, " +
            "c.clubName AS clubName," +
            "c.picturePath AS picturePath, " +
            "c.clubTotalDistance, " +
            "COUNT(uc.user.userId) AS memberCount " +
            "FROM Club c " +
            "LEFT JOIN UserClub uc ON c.clubId = uc.club.clubId " +
            "WHERE LOWER(c.clubName) LIKE %:search_name% " +
            "GROUP BY c.clubId")
    Page<ClubProjection> findAllClubPagination(@Param("search_name") String search_name,Pageable pageable);


    @Query("SELECT c.clubId AS clubId, " +
            "c.picturePath AS picturePath, " +
            "c.clubName AS clubName, " +
            "c.description AS description, " +
            "COUNT(uc.user.userId) AS totalMember, " +
            "c.clubTotalDistance AS totalDistance, " +
            "c.totalActivities AS totalActivities, " +
            "c.createdAt AS createdAt, " +
            "CONCAT(u.firstName, ' ', u.lastName) AS admin, " +
            "c.numOfMales AS numMales, " +
            "c.numOfFemales AS numFemales, " +
            "c.minPace AS minPace, " +
            "c.maxPace AS maxPace, " +
            "c.details AS details " +
            "FROM Club c " +
            "INNER JOIN UserClub uc ON c.clubId = uc.club.clubId " +
            "INNER JOIN User u ON c.adminUser.userId = u.userId " +
            "WHERE c.clubId = :clubId " +
            "GROUP BY c.clubId, " +
            "c.picturePath, " +
            "c.clubName, " +
            "c.description, " +
            "c.clubTotalDistance, " +
            "c.totalActivities, " +
            "c.createdAt, " +
            "u.firstName, u.lastName")
    ClubDetailProjection getClubDetails(@Param("clubId") int clubId);

    @Query("SELECT c.clubId AS clubId, " +
            "c.clubName AS clubName," +
            "c.picturePath AS picturePath, " +
            "c.clubTotalDistance, " +
            "COUNT(uc.user.userId) AS memberCount " +
            "FROM Club c " +
            "LEFT JOIN UserClub uc ON c.clubId = uc.club.clubId " +
            "WHERE c.creatorUser.userId = :creatorId " +
            "AND LOWER(c.clubName) LIKE %:search_name% " +
            "GROUP BY c.clubId")
    Page<ClubProjection> findOwnClubPagination(@Param("search_name") String search_name,Pageable pageable, @Param("creatorId") Long creatorId);


    @Query("SELECT c.clubId AS clubId, " +
            "c.clubName AS clubName," +
            "c.picturePath AS picturePath, " +
            "c.clubTotalDistance, " +
            "COUNT(uc.user.userId) AS memberCount " +
            "FROM Club c " +
            "LEFT JOIN UserClub uc ON c.clubId = uc.club.clubId " +
            "WHERE EXISTS (SELECT 1 FROM UserClub uc2 WHERE uc2.club.clubId = c.clubId " +
            "AND uc2.user.userId = :userId) " +
            "AND LOWER(c.clubName) LIKE %:search_name% " +
            "GROUP BY c.clubId")
    Page<ClubProjection> findClubJoined(@Param("search_name") String search_name,Pageable pageable, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Club c SET c.status = :mark, c.reason = :reason WHERE c.clubId = :clubId")
    void markLockClub(@Param("mark") String mark, @Param("clubId") Integer clubId, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query("UPDATE Club c SET c.outstanding = :mark WHERE c.clubId = :clubId")
    void markOutstandingClub(@Param("mark") String mark, @Param("clubId") Integer clubId);

    @Query("SELECT c FROM Club c WHERE unaccent(LOWER(c.clubName)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))")
    Page<Club> searchClubManage(@Param("searchName") String searchName, Pageable pageable);



}
