package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.User;
import com.be_uterace.entity.UserClub;
import com.be_uterace.utils.key.UserClubId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserClubRepository extends JpaRepository<UserClub, UserClubId> {

    // Tìm các bản ghi UserClub dựa trên clubId và userId
    @Query("SELECT uc FROM UserClub uc WHERE uc.club.clubId = :clubId AND uc.user.userId = :userId")
    Optional<UserClub> findByClubIdAndUserId(@Param("clubId") Integer clubId, @Param("userId") Long userId);

}
