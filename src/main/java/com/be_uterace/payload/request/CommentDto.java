package com.be_uterace.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Integer id;
    private String content;
    private Integer replyTo;
}
