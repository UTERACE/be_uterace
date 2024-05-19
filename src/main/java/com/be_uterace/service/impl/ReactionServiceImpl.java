package com.be_uterace.service.impl;

import com.be_uterace.entity.*;
import com.be_uterace.payload.request.ReactionDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.*;
import com.be_uterace.service.ReactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactionServiceImpl implements ReactionService {
    private final ReactionPostRepository reactionPostRepository;
    private final ReactionClubRepository reactionClubRepository;
    private final ReactionCommentRepository reactionCommentRepository;
    private final CommentPostRepository commentPostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;

    public ReactionServiceImpl(ReactionPostRepository reactionPostRepository, ReactionClubRepository reactionClubRepository, ReactionCommentRepository reactionCommentRepository, CommentPostRepository commentPostRepository, UserRepository userRepository, PostRepository postRepository, ClubRepository clubRepository, UserClubRepository userClubRepository) {
        this.reactionPostRepository = reactionPostRepository;
        this.reactionClubRepository = reactionClubRepository;
        this.reactionCommentRepository = reactionCommentRepository;
        this.commentPostRepository = commentPostRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.clubRepository = clubRepository;
        this.userClubRepository = userClubRepository;
    }

    @Override
    public ResponseObject addReaction(ReactionDto reactionDto) {
        Optional<User> user = authenticatedUser();
        Optional<Post> post = postRepository.findById(reactionDto.getId());
        if (!userClubRepository.existsByClubAndUser(post.orElseThrow().getClub(), user.orElseThrow())) {
            return new ResponseObject(401, "You are not authorized to react to this post");
        }
        if (!reactionPostRepository.existsByPostPostIdAndUserUserId(reactionDto.getId(), user.orElseThrow().getUserId())) {
            ReactionPost reactionPost = new ReactionPost(reactionDto.getReactionType());
            reactionPost.setUser(user.orElseThrow());
            reactionPost.setPost(post.orElseThrow());
            reactionPostRepository.save(reactionPost);
            return new ResponseObject(200, "Reaction added successfully");
        }
        return new ResponseObject(400, "Reaction already exists");
    }

    @Override
    public ResponseObject deleteReaction(Integer postId) {
        Optional<User> user = authenticatedUser();
        if (reactionPostRepository.existsByPostPostIdAndUserUserId(postId, user.orElseThrow().getUserId())) {
            ReactionPost reactionPost = reactionPostRepository.findByPostPostIdAndUserUserId(postId, user.orElseThrow().getUserId());
            reactionPostRepository.delete(reactionPost);
            return new ResponseObject(200, "Reaction deleted successfully");
        }
        return new ResponseObject(400, "Reaction does not exist");
    }

    @Override
    public ResponseObject addReactionClub(ReactionDto reactionDto) {
        Optional<User> user = authenticatedUser();
        if (!reactionClubRepository.existsByClubClubIdAndUserUserId(reactionDto.getId(), user.orElseThrow().getUserId())) {
            ReactionClub reactionClub = new ReactionClub(reactionDto.getReactionType());
            reactionClub.setUser(user.orElseThrow());
            Optional<Club> club = clubRepository.findById(reactionDto.getId());
            reactionClub.setClub(club.orElseThrow());
            reactionClubRepository.save(reactionClub);
            return new ResponseObject(200, "Reaction added successfully");
        }
        return new ResponseObject(400, "Reaction already exists");
    }

    @Override
    public ResponseObject deleteReactionClub(Integer clubId) {
        Optional<User> user = authenticatedUser();
        if (reactionClubRepository.existsByClubClubIdAndUserUserId(clubId, user.orElseThrow().getUserId())) {
            reactionClubRepository.deleteByClubClubIdAndUserUserId(clubId, user.orElseThrow().getUserId());
            return new ResponseObject(200, "Reaction deleted successfully");
        }
        return new ResponseObject(400, "Reaction does not exist");
    }

    @Override
    public ResponseObject addReactionComment(ReactionDto reactionDto) {
        Optional<User> user = authenticatedUser();
        if (!reactionCommentRepository.existsByCommentCommentIdAndUserUserId(reactionDto.getId(), user.orElseThrow().getUserId())) {
            ReactionComment reactionComment = new ReactionComment(reactionDto.getReactionType());
            reactionComment.setUser(user.orElseThrow());
            Optional<CommentPost> comment = commentPostRepository.findById(reactionDto.getId());
            reactionComment.setComment(comment.orElseThrow());
            reactionCommentRepository.save(reactionComment);
            return new ResponseObject(200, "Reaction added successfully");
        }
        return new ResponseObject(400, "Reaction already exists");
    }

    @Override
    public ResponseObject deleteReactionComment(Integer commentId) {
        Optional<User> user = authenticatedUser();
        if (reactionCommentRepository.existsByCommentCommentIdAndUserUserId(commentId, user.orElseThrow().getUserId())) {
            ReactionComment reactionComment = reactionCommentRepository.findByCommentCommentIdAndUserUserId(commentId, user.orElseThrow().getUserId());
            reactionCommentRepository.delete(reactionComment);
            return new ResponseObject(200, "Reaction deleted successfully");
        }
        return new ResponseObject(400, "Reaction does not exist");
    }

    private Optional<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }
}
