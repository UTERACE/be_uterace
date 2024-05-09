package com.be_uterace.repository;

import com.be_uterace.entity.CommentPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentPostRepository extends JpaRepository<CommentPost, Integer> {
    boolean existsByPostPostIdAndUserUserId(Integer postId, Long userId);
    CommentPost findByPostPostIdAndUserUserId(Integer postId, Long userId);
    void deleteByPostPostIdAndUserUserId(Integer postId, Long userId);
    int countByPostPostId(Integer postId);
    Page<CommentPost> findByPostPostIdAndReplyToIsNull(Integer postId, Pageable pageable);
    Page<CommentPost> findByReplyTo(Integer replyTo, Pageable pageable);
    int countByReplyTo(Integer replyTo);
}
