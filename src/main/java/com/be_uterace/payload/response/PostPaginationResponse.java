package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PostPaginationResponse {
    private int per_page;
    private int total_news;
    private int current_page;
    private int totalPage;
    List<PostResponse> news;
}
