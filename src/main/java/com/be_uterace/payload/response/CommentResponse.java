package com.be_uterace.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Integer comment_id;
    private String comment_content;
    private String comment_date;
    private String comment_update;
    private Integer count_likes;
    private Integer count_replies;
    private Integer reply_to;
    private Long user_id;
    private String user_name;
    private String user_avatar;
    private String user_role;
    private boolean is_liked;
}
