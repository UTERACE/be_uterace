package com.be_uterace.service;

import com.be_uterace.payload.request.CommentDto;
import com.be_uterace.payload.response.CommentResponse;
import com.be_uterace.payload.response.ResponseObject;

import java.util.List;

public interface CommentPostService {
    ResponseObject addComment(CommentDto commentDto);
    ResponseObject updateComment(CommentDto commentDto);
    ResponseObject deleteComment(Integer postId);
    List<CommentResponse> getComments(Integer postId, Integer page);
    List<CommentResponse> getCommentsByReplyTo(Integer replyTo, Integer page);
}
