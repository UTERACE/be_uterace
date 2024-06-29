package com.be_uterace.controller;

import com.be_uterace.payload.request.CreatePostDto;
import com.be_uterace.payload.request.UpdatePostDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/club/{club_id}")
    public ResponseEntity<List<PostClubPaginationResponse>> getPostClubController(@PathVariable Integer club_id,
                                                                                  @RequestParam int current_page,
                                                                                  @RequestParam int per_page, @RequestParam String search_name) {
        List<PostClubPaginationResponse> res = postService.getPostClub(club_id, current_page, per_page, search_name);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/my-news/{club_id}")
    public ResponseEntity<List<PostClubPaginationResponse>> getMyPostClubController(@PathVariable Integer club_id,
                                                                                  @RequestParam int current_page,
                                                                                  @RequestParam int per_page, @RequestParam String search_name) {
        List<PostClubPaginationResponse> res = postService.getMyPostClub(club_id, current_page, per_page, search_name);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/active-news/{club_id}")
    public ResponseEntity<List<PostClubPaginationResponse>> getActivePostClubController(@PathVariable Integer club_id,
                                                                                  @RequestParam int current_page,
                                                                                  @RequestParam int per_page, @RequestParam String search_name) {
        List<PostClubPaginationResponse> res = postService.getActivePostClub(club_id, current_page, per_page, search_name);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping()
    public ResponseEntity<PostPaginationResponse> getPostPaginationController(@RequestParam int current_page,
                                                                              @RequestParam int per_page, @RequestParam String search_name) {
        PostPaginationResponse res = postService.getPost(current_page, per_page, search_name);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/user")
    public ResponseEntity<PostPaginationResponse> getPostByUserCreatedController(@RequestParam int current_page,
                                                                                 @RequestParam int per_page, @RequestParam String search_name, Authentication auth) {
        PostPaginationResponse res = postService.getPostByUserCreated(current_page, per_page, search_name, auth);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{news_id}")
    public ResponseEntity<PostDetailResponse> getPostController(@PathVariable Integer news_id) {
        PostDetailResponse postResponse = postService.getPost(news_id);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> addPostController(@RequestBody CreatePostDto createPostDto, Authentication auth) {
        ResponseObject responseObject = postService.createPost(createPostDto, auth);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping()
    public ResponseEntity<ResponseObject> updatePostController(@RequestBody UpdatePostDto updatePostDto) {
        ResponseObject responseObject = postService.updatePost(updatePostDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<ResponseObject> deletePostController(@PathVariable Integer post_id, Authentication auth) {
        ResponseObject responseObject = postService.deletePost(post_id, auth);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/hide/{post_id}")
    public ResponseEntity<ResponseObject> hidePostController(@PathVariable Integer post_id, Authentication auth) {
        ResponseObject responseObject = postService.hidePost(post_id, auth);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/active/{post_id}/club/{club_id}")
    public ResponseEntity<ResponseObject> activePostController(@PathVariable Integer post_id, @PathVariable Integer club_id, Authentication auth) {
        ResponseObject responseObject = postService.activePost(post_id, club_id, auth);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
