package com.be_uterace.controller;

import com.be_uterace.payload.request.CommentDto;
import com.be_uterace.payload.response.CommentResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.CommentPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentPostService commentPostService;

    public CommentController(CommentPostService commentPostService) {
        this.commentPostService = commentPostService;
    }

    @GetMapping("/{post_id}")
    public List<CommentResponse> getComments(@PathVariable Integer post_id, @RequestParam Integer page) {
        return commentPostService.getComments(post_id, page);
    }

    @GetMapping("/reply/{reply_to}")
    public List<CommentResponse> getCommentsByReplyTo(@PathVariable Integer reply_to, @RequestParam Integer page) {
        return commentPostService.getCommentsByReplyTo(reply_to, page);
    }

    @PostMapping()
    public ResponseObject addComment(@RequestBody CommentDto commentDto) {
        return commentPostService.addComment(commentDto);
    }

    @PutMapping()
    public ResponseObject updateComment(@RequestBody CommentDto commentDto) {
        return commentPostService.updateComment(commentDto);
    }

    @DeleteMapping("/{post_id}")
    public ResponseObject deleteComment(@PathVariable Integer post_id) {
        return commentPostService.deleteComment(post_id);
    }
}
