package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Event;
import com.be_uterace.entity.Post;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.request.UpdatePostDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.PostService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    private UserRepository userRepository;

    private ClubRepository clubRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ClubRepository clubRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    public PostPaginationResponse getPost(int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        Page<Post> postPage = postRepository.findAllBy(pageable);

        List<Post> postList = postPage.getContent();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : postList){
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
    public PostDetailResponse getPost(Integer news_id) {
        Optional<Post> postOptional = postRepository.findById(news_id);
        Post post = postOptional.get();
        PostDetailResponse postResponse = new PostDetailResponse();
        postResponse.setNews_id(post.getPostId());
        postResponse.setImage(post.getImage());
        postResponse.setName(post.getTitle());
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
                if(createPostDto.getClub_id()==null){
                    post.setClub(null);
                }else {
                    Optional<Club> clubOptional = clubRepository.findById(createPostDto.getClub_id());
                    post.setClub(clubOptional.get());
                }

                post.setTitle(createPostDto.getTitle());
                post.setDescription(createPostDto.getDescription());
                post.setImage(createPostDto.getImage());
                post.setHtmlContent(createPostDto.getContent());
                post.setUserCreate(userOptional.get());
                postRepository.save(post);
                return new ResponseObject(StatusCode.SUCCESS,"Tạo bài viết thành công");

            }
        }

        return null;
    }

    @Override
    public ResponseObject updatePost(UpdatePostDto updatePostDto) {
        Optional<Post> postOptional = postRepository.findByClubIdAndPostId(updatePostDto.getNews_id(),
                updatePostDto.getClub_id());
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(updatePostDto.getTitle());
            post.setDescription(updatePostDto.getDescription());
            post.setImage(updatePostDto.getImage());
            post.setHtmlContent(post.getHtmlContent());
            // Lấy thời gian hiện tại
            LocalDateTime currentDateTime = LocalDateTime.now();
            post.setUpdatedAt(Timestamp.valueOf(currentDateTime));
            postRepository.save(post);
            return new ResponseObject(StatusCode.SUCCESS,"Cập nhât bài viết thành công");

        }
        return null;
    }

    @Override
    public ResponseObject deletePost(Integer post_id) {
        postRepository.deleteById(post_id);
        return new ResponseObject(StatusCode.SUCCESS,"Xóa bài viết thành công");
    }

}
