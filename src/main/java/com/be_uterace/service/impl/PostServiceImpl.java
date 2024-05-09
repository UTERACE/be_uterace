package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.request.UpdatePostDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.*;
import com.be_uterace.service.FileService;
import com.be_uterace.service.PostService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final FileService fileService;

    private final ReactionPostRepository reactionPostRepository;
    private final CommentPostRepository commentPostRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ClubRepository clubRepository, FileService fileService, ReactionPostRepository reactionPostRepository, CommentPostRepository commentPostRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.fileService = fileService;
        this.reactionPostRepository = reactionPostRepository;
        this.commentPostRepository = commentPostRepository;
    }

    @Override
    public PostPaginationResponse getPost(int current_page, int per_page, String search_name) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Post> postPage = postRepository.findAllByTitleContaining(search_name, pageable);

        List<Post> postList = postPage.getContent();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : postList) {
            PostResponse item = new PostResponse();
            item.setNews_id(post.getPostId());
            item.setName(post.getTitle());
            item.setDescription(post.getDescription());
            item.setImage(post.getImage());
            item.setCreated_at(post.getCreatedAt());
            item.setUpdated_at(post.getUpdatedAt());
            postResponses.add(item);
        }
        return PostPaginationResponse.builder()
                .per_page(postPage.getSize())
                .total_news((int) postPage.getTotalElements())
                .current_page(postPage.getNumber() + 1)
                .totalPage(postPage.getTotalPages())
                .news(postResponses)
                .build();
    }

    @Override
    public PostPaginationResponse getPostByUserCreated(int current_page, int per_page, String search_name, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Pageable pageable = PageRequest.of(current_page - 1, per_page);
                Page<Post> postPage = postRepository.getPostsByTitleContainingAndUserCreateUserId(search_name, pageable, userOptional.get().getUserId().intValue());
                List<Post> postList = postPage.getContent();
                List<PostResponse> postResponses = new ArrayList<>();
                for (Post post : postList) {
                    PostResponse item = new PostResponse();
                    item.setNews_id(post.getPostId());
                    item.setName(post.getTitle());
                    item.setDescription(post.getDescription());
                    item.setImage(post.getImage());
                    item.setCreated_at(post.getCreatedAt());
                    item.setUpdated_at(post.getUpdatedAt());
                    item.setOutstanding(post.getOutstanding());
                    postResponses.add(item);
                }
                return PostPaginationResponse.builder()
                        .per_page(postPage.getSize())
                        .total_news((int) postPage.getTotalElements())
                        .current_page(postPage.getNumber() + 1)
                        .totalPage(postPage.getTotalPages())
                        .news(postResponses)
                        .build();
            }
        }
        return null;
    }

    @Override
    public PostDetailResponse getPost(Integer news_id) {
        Optional<Post> postOptional = postRepository.findById(news_id);
        Post post = postOptional.get();
        PostDetailResponse postResponse = new PostDetailResponse();
        postResponse.setNews_id(post.getPostId());
        postResponse.setImage(post.getImage());
        postResponse.setName(post.getTitle());
        postResponse.setOutstanding(post.getOutstanding().toString());
        postResponse.setDescription(post.getDescription());
        postResponse.setContent(post.getHtmlContent());
        return postResponse;
    }

    @Override
    public ResponseObject createPost(CreatePostDto createPostDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Post post = new Post();
                if (createPostDto.getClub_id() == null) {
                    post.setClub(null);
                } else {
                    Optional<Club> clubOptional = clubRepository.findById(createPostDto.getClub_id());
                    post.setClub(clubOptional.get());
                }

                post.setTitle(createPostDto.getTitle());
                post.setDescription(createPostDto.getDescription());
                if (!Objects.equals(createPostDto.getImage(), ""))
                    post.setImage(fileService.saveImage(createPostDto.getImage()));
                else
                    post.setImage("");
                post.setHtmlContent(createPostDto.getContent());
                post.setUserCreate(userOptional.get());
                post.setOutstanding("0");
                post.setStatus("1");
                postRepository.save(post);
                return new ResponseObject(StatusCode.SUCCESS, "Tạo bài viết thành công");

            }
        }

        return null;
    }

    @Override
    public ResponseObject updatePost(UpdatePostDto updatePostDto) {
        Optional<Post> postOptional;
        if (updatePostDto.getClub_id() != null) {
            postOptional = postRepository.findByClubIdAndPostId(updatePostDto.getNews_id(),
                    updatePostDto.getClub_id());
        } else {
            postOptional = postRepository.findById(updatePostDto.getNews_id());
        }
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(updatePostDto.getTitle() != null && !Objects.equals(updatePostDto.getTitle(), "") ?
                    updatePostDto.getTitle() : post.getTitle());
            post.setDescription(updatePostDto.getDescription() != null && !Objects.equals(updatePostDto.getDescription(), "") ?
                    updatePostDto.getDescription() : post.getDescription());
            if (!post.getImage().equals(updatePostDto.getImage()) && !Objects.equals(updatePostDto.getImage(), "")) {
                if (Objects.equals(post.getImage(), "")) {
                    post.setImage(fileService.saveImage(updatePostDto.getImage()));
                } else if (fileService.deleteImage(post.getImage())) {
                    System.out.println("Delete image success");
                    post.setImage(fileService.saveImage(updatePostDto.getImage()));
                }
            }
            post.setHtmlContent(updatePostDto.getContent() != null && !Objects.equals(updatePostDto.getContent(), "") ?
                    updatePostDto.getContent() : post.getHtmlContent());
            // Lấy thời gian hiện tại
            LocalDateTime currentDateTime = LocalDateTime.now();
            post.setUpdatedAt(Timestamp.valueOf(currentDateTime));
            postRepository.save(post);
            return new ResponseObject(StatusCode.SUCCESS, "Cập nhât bài viết thành công");

        }
        return null;
    }

    @Override
    public ResponseObject deletePost(Integer post_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Post> postOptional = postRepository.findById(post_id);
                if (postOptional.isPresent()) {
                    Post post = postOptional.get();
                    if (post.getUserCreate().getUserId().equals(userOptional.get().getUserId())) {
                        postRepository.delete(post);
                        return new ResponseObject(StatusCode.SUCCESS, "Xóa bài viết thành công");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ResponseObject hidePost(Integer post_id, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                Optional<Post> postOptional = postRepository.findById(post_id);
                if (postOptional.isPresent()) {
                    Post post = postOptional.get();
                    post.setStatus("0");
                    postRepository.save(post);
                    return new ResponseObject(StatusCode.SUCCESS, "Ẩn bài viết thành công");
                }
            }
        }
        return null;
    }

    @Override
    public List<PostClubPaginationResponse> getPostClub(int club_id, int current_page, int per_page, String search_name) {
        Optional<User> userOptional = authenticatedUser();
        List<Post> postList = postRepository.getPostsByClubClubIdAndTitleContaining(club_id, search_name, PageRequest.of(current_page - 1, per_page)).getContent();
        List<PostClubPaginationResponse> postClubPaginationResponses = new ArrayList<>();
        for (Post post : postList) {
            PostClubPaginationResponse postClubPaginationResponse = new PostClubPaginationResponse();
            postClubPaginationResponse.setPost_id(post.getPostId());
            postClubPaginationResponse.setPost_title(post.getTitle());
            postClubPaginationResponse.setPost_content(post.getDescription());
            postClubPaginationResponse.setPost_description(post.getHtmlContent());
            postClubPaginationResponse.setPost_image(post.getImage());
            postClubPaginationResponse.setPost_date(post.getCreatedAt().toString());
            postClubPaginationResponse.setPost_outstanding(post.getOutstanding());
            postClubPaginationResponse.setPost_status(post.getStatus());
            boolean isLiked = reactionPostRepository.existsByPostPostIdAndReactionTypeAndUserUserId(post.getPostId(), "like", userOptional.orElseThrow().getUserId());
            postClubPaginationResponse.set_liked(isLiked);
            int countLikes = reactionPostRepository.countByPostPostId(post.getPostId());
            postClubPaginationResponse.setCount_likes(countLikes);
            int countComments = commentPostRepository.countByPostPostId(post.getPostId());
            postClubPaginationResponse.setCount_comments(countComments);
            boolean isAdmin = clubRepository.existsByAdminUserUserId(post.getUserCreate().getUserId());
            boolean isOwner = clubRepository.existsByCreatorUserUserId(post.getUserCreate().getUserId());
            postClubPaginationResponse.setUser_id(post.getUserCreate().getUserId());
            postClubPaginationResponse.setUser_name(post.getUserCreate().getFirstName() + " " + post.getUserCreate().getLastName());
            postClubPaginationResponse.setUser_avatar(post.getUserCreate().getAvatarPath());
            postClubPaginationResponse.setUser_role(isAdmin ? "admin" : isOwner ? "owner" : "member");
            postClubPaginationResponses.add(postClubPaginationResponse);
        }
        return postClubPaginationResponses;
    }

    private Optional<User> authenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }
}
