package com.be_uterace.repository;

import com.be_uterace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
