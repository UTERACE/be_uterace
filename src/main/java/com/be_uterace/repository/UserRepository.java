package com.be_uterace.repository;

import com.be_uterace.entity.User;
import com.be_uterace.payload.response.UserRankingResponse;
import com.be_uterace.projection.UserRankingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

//    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    List<User> findTop8ByOrderByRankingAsc();

    @Query("SELECT SUM(u.totalDistance) FROM User u")
    Double sumTotalDistance();

    @Query("SELECT u.userId as userId " +
            ", u.firstName as firstName, " +
            "u.lastName as lastName, " +
            "u.avatarPath as avatarPath, " +
            "u.gender as gender, " +
            "COALESCE(AVG(r.pace), 0) AS pace, COALESCE(SUM(r.distance), 0) AS totalDistance, " +
            "ROW_NUMBER() OVER (ORDER BY COALESCE(SUM(r.distance), 0) DESC) AS ranking, " +
            "u.organization.orgId as organization " +
            "FROM User u " +
            "LEFT JOIN Run r ON u.userId = r.user.userId " +
            "WHERE (:month = 0 OR EXTRACT(MONTH FROM r.createdAt) = :month) " +
            "AND (:year = 0 OR EXTRACT(YEAR FROM r.createdAt) = :year) " +
            "GROUP BY u.userId")
    Page<UserRankingProjection> findScoreboardUser(
            @Param("month") int month,
            @Param("year") int year,
            Pageable pageable
    );

}
