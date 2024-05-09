package com.be_uterace.repository;

import com.be_uterace.entity.ReactionClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionClubRepository extends JpaRepository<ReactionClub, Integer> {
    boolean existsByClubClubIdAndUserUserId(Integer clubId, Long userId);
    void deleteByClubClubIdAndUserUserId(Integer clubId, Long userId);
    int countByClubClubId(Integer clubId);
}
