package com.be_uterace.repository;

import com.be_uterace.entity.CommentPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentPostRepository extends JpaRepository<CommentPost, Integer> {
    boolean existsByCommentIdAndUserUserId(Integer commentId, Long userId);
    CommentPost findByCommentIdAndUserUserId(Integer postId, Long userId);
    int countByPostPostId(Integer postId);
    Page<CommentPost> findByPostPostIdAndReplyToIsNullAndStatusOrderByCreatedAtAsc(Integer postId, Pageable pageable, String status);
    Page<CommentPost> findByReplyTo(Integer replyTo, Pageable pageable);
    int countByReplyTo(Integer replyTo);
}
