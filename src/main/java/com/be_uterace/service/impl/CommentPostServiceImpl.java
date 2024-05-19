package com.be_uterace.service.impl;

import com.be_uterace.entity.CommentPost;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CommentDto;
import com.be_uterace.payload.response.CommentResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.*;
import com.be_uterace.service.CommentPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentPostServiceImpl implements CommentPostService {
    private final CommentPostRepository commentPostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReactionCommentRepository reactionCommentRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;

    public CommentPostServiceImpl(CommentPostRepository commentPostRepository, UserRepository userRepository, PostRepository postRepository, ReactionCommentRepository reactionCommentRepository, ClubRepository clubRepository, UserClubRepository userClubRepository) {
        this.commentPostRepository = commentPostRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.reactionCommentRepository = reactionCommentRepository;
        this.clubRepository = clubRepository;
        this.userClubRepository = userClubRepository;
    }

    public ResponseObject addComment(CommentDto commentDto) {
        try {
            Optional<User> user = authenticatedUser();
            Optional<Post> post = postRepository.findById(commentDto.getId());
            if (userClubRepository.existsByClubAndUser(post.orElseThrow().getClub(), user.orElseThrow())) {
                CommentPost commentPost = new CommentPost(commentDto.getContent());
                if (commentDto.getReplyTo() != null) {
                    commentPost.setReplyTo(commentDto.getReplyTo());
                }
                commentPost.setPost(post.orElseThrow());
                commentPost.setUser(user.orElseThrow());
                commentPostRepository.save(commentPost);
                return new ResponseObject(200, "Comment added successfully");
            }
            return new ResponseObject(401, "You are not authorized to comment on this post");
        } catch (Exception e) {
            return new ResponseObject(400, "Error adding comment");
        }
    }

    @Override
    public ResponseObject updateComment(CommentDto commentDto) {
        Optional<User> user = authenticatedUser();
        if (commentPostRepository.existsByCommentIdAndUserUserId(commentDto.getId(), user.orElseThrow().getUserId())) {
            CommentPost commentPost = commentPostRepository.findByCommentIdAndUserUserId(commentDto.getId(), user.orElseThrow().getUserId());
            commentPost.setCommentContent(commentDto.getContent());
            commentPostRepository.save(commentPost);
            return new ResponseObject(200, "Comment updated successfully");
        }
        return new ResponseObject(400, "Comment does not exist");
    }

    public ResponseObject deleteComment(Integer comment_id) {
        Optional<User> user = authenticatedUser();
        if (commentPostRepository.existsById(comment_id)) {
            CommentPost commentPost = commentPostRepository.findById(comment_id).orElseThrow();
            if (commentPost.getUser().getUserId().equals(user.orElseThrow().getUserId())) {
                reactionCommentRepository.deleteAll(reactionCommentRepository.findByComment(commentPost));
                commentPostRepository.delete(commentPost);
                return new ResponseObject(200, "Comment deleted successfully");
            }
            return new ResponseObject(400, "You are not authorized to delete this comment");
        }
        return new ResponseObject(400, "Comment does not exist");
    }

    @Override
    public ResponseObject hideComment(Integer postId) {
        Optional<User> user = authenticatedUser();
        CommentPost commentPost = commentPostRepository.findById(postId).orElseThrow();
        if (clubRepository.existsByAdminUserUserIdAndClubId(user.orElseThrow().getUserId(), commentPost.getPost().getClub().getClubId())) {
            commentPost.setStatus("1");
            commentPostRepository.save(commentPost);
            return new ResponseObject(200, "Comment hidden successfully");
        }
        return new ResponseObject(400, "Comment does not exist");
    }

    @Override
    public ResponseObject unhideComment(Integer postId) {
        Optional<User> user = authenticatedUser();
        CommentPost commentPost = commentPostRepository.findById(postId).orElseThrow();
        if (clubRepository.existsByAdminUserUserIdAndClubId(user.orElseThrow().getUserId(), commentPost.getPost().getClub().getClubId())) {
            commentPost.setStatus("0");
            commentPostRepository.save(commentPost);
            return new ResponseObject(200, "Comment unhidden successfully");
        }
        return new ResponseObject(400, "Comment does not exist");
    }

    @Override
    public ResponseObject reportComment(Integer postId) {
        return null;
    }

    @Override
    public List<CommentResponse> getComments(Integer postId, Integer page) {
        Page<CommentPost> commentPosts = commentPostRepository.findByPostPostIdAndReplyToIsNullAndStatusOrderByCreatedAtAsc(postId, Pageable.ofSize(10).withPage(page - 1), "0");
        return commentResponses(commentPosts);
    }

    @Override
    public List<CommentResponse> getCommentsByReplyTo(Integer replyTo, Integer page) {
        Page<CommentPost> commentPosts = commentPostRepository.findByReplyTo(replyTo, Pageable.ofSize(10).withPage(page - 1));
        return commentResponses(commentPosts);
    }

    private Optional<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

    private List<CommentResponse> commentResponses(Page<CommentPost> commentPosts) {
        Optional<User> user = authenticatedUser();
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentPost commentPost : commentPosts) {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setComment_id(commentPost.getCommentId());
            commentResponse.setComment_content(commentPost.getCommentContent());
            commentResponse.setComment_date(commentPost.getCreatedAt().toString());
            commentResponse.setComment_update(commentPost.getUpdatedAt() != null ? commentPost.getUpdatedAt().toString() : null);
            int count_replies = commentPostRepository.countByReplyTo(commentPost.getCommentId());
            commentResponse.setCount_replies(count_replies);
            int count_likes = reactionCommentRepository.countByCommentCommentId(commentPost.getCommentId());
            commentResponse.setCount_likes(count_likes);
            commentResponse.setReply_to(commentPost.getReplyTo());
            commentResponse.setUser_id(commentPost.getUser().getUserId());
            commentResponse.setUser_name(commentPost.getUser().getFirstName() + " " + commentPost.getUser().getLastName());
            commentResponse.setUser_avatar(commentPost.getUser().getAvatarPath());
            boolean isAdmin = clubRepository.existsByAdminUserUserIdAndClubId(commentPost.getUser().getUserId(), commentPost.getPost().getClub().getClubId());
            boolean isOwner = clubRepository.existsByCreatorUserUserIdAndClubId(commentPost.getUser().getUserId(), commentPost.getPost().getClub().getClubId());
            commentResponse.setUser_role(isAdmin ? "admin" : isOwner ? "owner" : "member");
            boolean is_liked = reactionCommentRepository.existsByCommentCommentIdAndUserUserId(commentPost.getCommentId(), user.orElseThrow().getUserId());
            commentResponse.set_liked(is_liked);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }
}
