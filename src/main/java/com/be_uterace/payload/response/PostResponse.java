package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer news_id;
    private String name;
    private String description;
    private String image;
    private Date created_at;
    private Date updated_at;
    private boolean outstanding;
    private boolean deleted;
}
