package com.be_uterace.service;

import com.be_uterace.payload.request.ReactionDto;
import com.be_uterace.payload.response.ReactionResponse;
import com.be_uterace.payload.response.ResponseObject;

public interface ReactionService {
    ResponseObject addReaction( ReactionDto reactionDto);
    ResponseObject deleteReaction(Integer postId);
    ResponseObject addReactionClub(ReactionDto reactionDto);
    ResponseObject deleteReactionClub(Integer clubId);
    ResponseObject addReactionComment(ReactionDto reactionDto);
    ResponseObject deleteReactionComment(Integer commentId);
    ReactionResponse getReactions(Integer clubId);
}
