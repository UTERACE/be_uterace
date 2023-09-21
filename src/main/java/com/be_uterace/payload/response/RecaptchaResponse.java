package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecaptchaResponse {
    private boolean success;
    private String challenge_ts;
    private String hostname;
    private float score;
    private String action;
    @JsonCreator
    public RecaptchaResponse(@JsonProperty("success") boolean success,
                             @JsonProperty("challenge_ts") String challenge_ts,
                             @JsonProperty("hostname") String hostname,
                             @JsonProperty("score") float score,
                             @JsonProperty("action") String action) {
        this.success = success;
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.score = score;
        this.action = action;
    }
}
