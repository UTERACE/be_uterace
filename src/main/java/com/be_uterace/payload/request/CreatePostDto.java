package com.be_uterace.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {
    private String title;
    private String description;
    private String image;
    private String content;
    private Integer club_id;
}
