package com.be_uterace.repository;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.PostHomeResponse;
import com.be_uterace.payload.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p WHERE p.userCreate = (SELECT c.adminUser FROM Club c WHERE c.clubId = :clubId)")
    List<Post> findPostsCreatedByClubAdmin(@Param("clubId") Integer clubId);
    @Query("SELECT  new com.be_uterace.payload.response.PostHomeResponse(p.postId, p.title, p.image, p.description) " +
            "FROM Post p " +
            "WHERE p.outstanding = '1' AND p.status = '1' " +
            "ORDER BY p.createdAt DESC " +
            "LIMIT 6")
    List<PostHomeResponse> findTop6ClubsByOutstandingAndCreatedAtContaining();
    List<Post> findTop6PostsByOutstanding(String outstanding);
    List<Post> getPostsByClubClubId(Integer clubId);
    Page<Post> getPostsByTitleContainingAndUserCreateUserId(String search_name,Pageable pageable,Integer userId);

    @Query("SELECT p FROM Post p WHERE p.status='1' " +
            "AND unaccent(LOWER(p.title)) LIKE unaccent(LOWER(concat('%', :search_name, '%'))) " +
            "ORDER BY p.createdAt DESC ")
    Page<Post> findAllByTitleContaining(String search_name,Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postId = :postId and p.club.clubId=:clubId")
    Optional<Post> findByClubIdAndPostId(@Param("postId") Integer postId, @Param("clubId") Integer clubId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.status = :mark, p.reason = :reason WHERE p.postId = :postId")
    void markLockPost(@Param("mark") String mark, @Param("postId") Integer postId, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.outstanding = :mark WHERE p.postId = :postId")
    void markOutstandingPost(@Param("mark") String mark, @Param("postId") Integer postId);

    @Query("SELECT p FROM Post p WHERE unaccent(LOWER(p.title)) LIKE unaccent(LOWER(concat('%', :searchName, '%')))")
    Page<Post> searchPostManage(@Param("searchName") String searchName, Pageable pageable);

    Page<Post> getPostsByClubClubIdAndTitleContaining(Integer clubId, String search_name, Pageable pageable);
}
