package com.be_uterace.repository;


import com.be_uterace.entity.User;
import com.be_uterace.entity.UserClub;
import com.be_uterace.entity.UserEvent;
import com.be_uterace.utils.key.UserClubId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserClubRepository extends JpaRepository<UserClub, UserClubId> {

    // Tìm các bản ghi UserClub dựa trên clubId và userId
    @Query("SELECT uc FROM UserClub uc WHERE uc.club.clubId = :clubId AND uc.user.userId = :userId")
    Optional<UserClub> findByClubIdAndUserId(@Param("clubId") Integer clubId, @Param("userId") Long userId);


    @Query("SELECT COUNT(uc) FROM UserClub uc WHERE uc.user.userId = :userId")
    int countClubsByUserId(@Param("userId") Long userId);


    @Query("SELECT uc FROM UserClub uc " +
            "INNER JOIN uc.user u " +
            "WHERE uc.club.clubId = :clubId " +
            "AND (:searchName IS NULL " +
            "OR (unaccent(LOWER(u.firstName)) ILIKE unaccent(LOWER(:searchName)) " +
            "OR unaccent(LOWER(u.lastName)) ILIKE unaccent(LOWER(:searchName))))" +
            "ORDER BY uc.ranking ASC")
    Page<UserClub> findByClubIdAndSearchName(
            @Param("clubId") Integer clubId,
            @Param("searchName") String searchName,
            Pageable pageable
    );


}
