package com.be_uterace.controller;

import com.be_uterace.payload.request.ReactionDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.ReactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reaction")
public class ReactionController {
    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping("/club")
    public ResponseObject addReactionClub(@RequestBody ReactionDto reactionDto) {
        return reactionService.addReactionClub(reactionDto);
    }

    @DeleteMapping("/club/{club_id}")
    public ResponseObject deleteReactionClub(@PathVariable Integer club_id) {
        return reactionService.deleteReactionClub(club_id);
    }

    @PostMapping("/post")
    public ResponseObject addReaction(@RequestBody ReactionDto reactionDto) {
        return reactionService.addReaction(reactionDto);
    }

    @DeleteMapping("/post/{post_id}")
    public ResponseObject deleteReaction(@PathVariable Integer post_id) {
        return reactionService.deleteReaction(post_id);
    }

    @PostMapping("/comment")
    public ResponseObject addReactionComment(@RequestBody ReactionDto reactionDto) {
        return reactionService.addReactionComment(reactionDto);
    }

    @DeleteMapping("/comment/{comment_id}")
    public ResponseObject deleteReactionComment(@PathVariable Integer comment_id) {
        return reactionService.deleteReactionComment(comment_id);
    }
}
