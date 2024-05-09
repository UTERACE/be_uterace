package com.be_uterace.controller;

import com.be_uterace.payload.request.ReactionDto;
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
    public void addReactionClub(@RequestBody ReactionDto reactionDto) {
        reactionService.addReactionClub(reactionDto);
    }

    @DeleteMapping("/club/{club_id}")
    public void deleteReactionClub(@PathVariable Integer club_id) {
        reactionService.deleteReactionClub(club_id);
    }

    @PostMapping("/post")
    public void addReaction(@RequestBody ReactionDto reactionDto) {
        reactionService.addReaction(reactionDto);
    }

    @DeleteMapping("/post/{post_id}")
    public void deleteReaction(@PathVariable Integer post_id) {
        reactionService.deleteReaction(post_id);
    }

    @PostMapping("/comment")
    public void addReactionComment(@RequestBody ReactionDto reactionDto) {
        reactionService.addReactionComment(reactionDto);
    }

    @DeleteMapping("/comment/{comment_id}")
    public void deleteReactionComment(@PathVariable Integer comment_id) {
        reactionService.deleteReactionComment(comment_id);
    }
}
