package com.be_uterace.payload.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ManagePostSearchResponse {
    private int per_page;

    private int current_page;

    private int total_page;

    private int total_news;

    private List<NewsItem> news;

    // getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewsItem {
        private Integer news_id;

        private String name;

        private String image;

        private String description;

        private Date createdAt;

        private Date updatedAt;

        private String outstanding;

        private String status;

        private String reason_block;

    }
}
