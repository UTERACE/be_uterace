package com.be_uterace.service;

import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.request.UpdatePostDto;
import com.be_uterace.payload.response.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostService {
    PostPaginationResponse getPost(int current_page, int per_page, String search_name);

    PostPaginationResponse getPostByUserCreated(int current_page, int per_page, String search_name, Authentication authentication);

    PostDetailResponse getPost(Integer news_id);

    ResponseObject createPost(CreatePostDto createPostDto, Authentication authentication);

    ResponseObject updatePost(UpdatePostDto updatePostDto);

    ResponseObject deletePost(Integer post_id, Authentication authentication);

    ResponseObject hidePost(Integer post_id, Authentication authentication);

    ResponseObject activePost(Integer post_id, Integer club_id, Authentication authentication);

    List<PostClubPaginationResponse> getPostClub(int club_id, int current_page, int per_page, String search_name);

    List<PostClubPaginationResponse> getMyPostClub(int club_id, int current_page, int per_page, String search_name);

    List<PostClubPaginationResponse> getActivePostClub(int club_id, int current_page, int per_page, String search_name);
}
