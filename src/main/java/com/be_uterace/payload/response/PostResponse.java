package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
@Getter
@Setter
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
    private String outstanding;
    private String status;
}
