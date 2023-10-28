package com.be_uterace.service;

import com.be_uterace.payload.response.PostDetailResponse;
import com.be_uterace.payload.response.PostPaginationResponse;
import com.be_uterace.payload.response.PostResponse;

public interface PostService {
    PostPaginationResponse getPost(int current_page, int per_page);

    PostDetailResponse getPost(Integer news_id);
}
