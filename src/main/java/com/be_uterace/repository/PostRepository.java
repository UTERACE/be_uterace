package com.be_uterace.repository;

import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p WHERE p.userCreate = (SELECT c.adminUser FROM Club c WHERE c.clubId = :clubId)")
    List<Post> findPostsCreatedByClubAdmin(@Param("clubId") Integer clubId);

    Page<Post> findAllBy(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userCreate = (SELECT c.adminUser FROM Club c WHERE c.clubId = :clubId)")
    Optional<Post> findByClubIdAndPostId();
}
