package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {
    private Integer news_id;
    private String name;
    private String description;
    private String image;
    private String content;
}
