package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostHomeResponse {
    private Integer news_id;
    private String name;
    private String image;
    private String description;
}
