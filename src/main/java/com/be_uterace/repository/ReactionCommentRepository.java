package com.be_uterace.repository;

import com.be_uterace.entity.CommentPost;
import com.be_uterace.entity.ReactionComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionCommentRepository extends JpaRepository<ReactionComment, Integer>{
    boolean existsByCommentCommentIdAndUserUserId(Integer commentId, Long userId);
    ReactionComment findByCommentCommentIdAndUserUserId(Integer commentId, Long userId);
    int countByCommentCommentId(Integer commentId);
    List<ReactionComment> findByComment(CommentPost comment);
}