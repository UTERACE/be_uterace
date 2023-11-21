package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.Post;
import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManagePostSearchResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.service.ManagePostService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagePostServiceImpl implements ManagePostService {

    private PostRepository postRepository;

    public ManagePostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public ResponseObject lockPost(Integer post_id) {
        postRepository.markLockPost("0",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa bài viết thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockPost(Integer post_id) {
        postRepository.markLockPost("1",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Mở khóa bài viết thành công");
        return responseObject;
    }

    @Override
    public ResponseObject outstandingPost(Integer post_id) {
        postRepository.markOutstandingPost("1",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Chọn bài viết nổi bật thành công");
        return responseObject;
    }

    @Override
    public ResponseObject notOutstandingPost(Integer post_id) {
        postRepository.markOutstandingPost("0",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa bài viết nổi bật thành công");
        return responseObject;
    }

    @Override
    public ManagePostSearchResponse searchPost(int current_page, int per_page, String search) {
        Page<Post> postPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        if(search==null || search.equals("")) {
            postPage = postRepository.findAll(pageable);
        } else {
            postPage = postRepository.searchPostManage(search,pageable);
        }
        List<Post> postList = postPage.getContent();
        List<ManagePostSearchResponse.NewsItem> postListResponse = new ArrayList<>();
        for (Post post : postList){
            ManagePostSearchResponse.NewsItem postResponse = new ManagePostSearchResponse.NewsItem();
            postResponse.setNews_id(post.getPostId());
            postResponse.setName(post.getTitle());
            postResponse.setImage(post.getImage());
            postResponse.setDescription(post.getDescription());
            postResponse.setCreatedAt(post.getCreatedAt());
            postResponse.setUpdatedAt(post.getUpdatedAt());
            postResponse.setStatus(post.getStatus());
            postResponse.setReason_block(post.getReason());
            postListResponse.add(postResponse);
        }
        return ManagePostSearchResponse.builder()
                .per_page(postPage.getSize())
                .total_news((int) postPage.getTotalElements())
                .current_page(postPage.getNumber() + 1)
                .total_page(postPage.getTotalPages())
                .news(postListResponse)
                .build();

    }
}
