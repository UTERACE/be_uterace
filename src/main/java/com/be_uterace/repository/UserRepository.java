package com.be_uterace.repository;

import com.be_uterace.entity.User;
import com.be_uterace.payload.response.RankingUserHomeResponse;
import com.be_uterace.projection.UserRankingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

//    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByStravaId(Long stravaId);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    List<User> findTop8ByOrderByRankingAsc();

    @Query("SELECT SUM(u.totalDistance) FROM User u")
    Double sumTotalDistance();

    @Query(nativeQuery = true, value = "SELECT * FROM GetRankedUsers(:month,:year,:searchName)")
    Page<UserRankingProjection> findScoreboardUser(
            @Param("month") int month,
            @Param("year") int year,
            @Param("searchName") String searchName,
            Pageable pageable
    );
    @Query("SELECT new com.be_uterace.payload.response.RankingUserHomeResponse(u.userId,u.ranking, u.firstName, u.lastName, u.avatarPath, u.totalDistance, u.pace) " +
            "FROM User u " +
            "WHERE u.status = '1' " +
            "ORDER BY u.totalDistance DESC " +
            "LIMIT 8")
    List<RankingUserHomeResponse> findTop8ByOrderByTotalDistanceAsc();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :mark, u.reason = :reason WHERE u.userId = :userId")
    void markLockUser(@Param("mark") String mark, @Param("userId") Integer userId, @Param("reason") String reason);


    @Query("SELECT u FROM User u " +
            "WHERE unaccent(LOWER(u.firstName)) ILIKE unaccent(LOWER(:searchName)) " +
            "OR unaccent(LOWER(u.lastName)) ILIKE unaccent(LOWER(:searchName))")
    Page<User> searchUsers(@Param("searchName") String searchName, Pageable pageable);


}
