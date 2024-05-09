package com.be_uterace.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostClubPaginationResponse {
    private int post_id;
    private String post_title;
    private String post_content;
    private String post_description;
    private String post_date;
    private String post_image;
    private String post_outstanding;
    private String post_status;
    private int count_likes;
    private int count_comments;
    private boolean is_liked;
    private Long user_id;
    private String user_name;
    private String user_avatar;
    private String user_role;
}
