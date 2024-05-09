package com.be_uterace.repository;

import com.be_uterace.entity.ReactionPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionPostRepository extends JpaRepository<ReactionPost, Integer> {
    boolean existsByPostPostIdAndUserUserId(Integer postId, Long userId);

    ReactionPost findByPostPostIdAndUserUserId(Integer postId, Long userId);

    int countByPostPostId(Integer postId);

    boolean existsByPostPostIdAndReactionTypeAndUserUserId(Integer postId, String reactionType, Long userId);
}
