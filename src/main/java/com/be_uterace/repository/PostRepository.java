package com.be_uterace.repository;

import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p WHERE p.userCreate = (SELECT c.adminUser FROM Club c WHERE c.clubId = :clubId)")
    List<Post> findPostsCreatedByClubAdmin(@Param("clubId") Integer clubId);
    List<Post> getPostsByClubClubId(Integer clubId);
    Page<Post> getPostsByTitleContainingAndUserCreateUserId(String search_name,Pageable pageable,Integer userId);

    Page<Post> findAllByTitleContaining(String search_name,Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postId = :postId and p.club.clubId=:clubId")
    Optional<Post> findByClubIdAndPostId(@Param("postId") Integer postId, @Param("clubId") Integer clubId);

    @Modifying
    @Query("UPDATE Post p SET p.deleted = :mark WHERE p.postId = :postId")
    void markPost(@Param("mark") String mark, @Param("postId") Integer postId);
}
