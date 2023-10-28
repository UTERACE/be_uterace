package com.be_uterace.service.impl;

import com.be_uterace.entity.Event;
import com.be_uterace.entity.Post;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostPaginationResponse getPost(int current_page, int per_page) {
        Pageable pageable = PageRequest.of(current_page, per_page);
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
                .current_page(postPage.getNumber())
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
        postResponse.setContent(postResponse.getContent());
        return postResponse;
    }
}
