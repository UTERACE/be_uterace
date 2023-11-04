package com.be_uterace.service;

import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.request.UpdatePostDto;
import com.be_uterace.payload.response.PostDetailResponse;
import com.be_uterace.payload.response.PostPaginationResponse;
import com.be_uterace.payload.response.PostResponse;
import com.be_uterace.payload.response.ResponseObject;
import org.springframework.security.core.Authentication;

public interface PostService {
    PostPaginationResponse getPost(int current_page, int per_page);

    PostPaginationResponse getPostByUserCreated(int current_page, int per_page, Authentication authentication);

    PostDetailResponse getPost(Integer news_id);

    ResponseObject createPost(CreatePostDto createPostDto, Authentication authentication);

    ResponseObject updatePost(UpdatePostDto updatePostDto);

    ResponseObject deletePost(Integer post_id, Authentication authentication);

    ResponseObject hidePost(Integer post_id, Authentication authentication);
}
