package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StravaOauthResponse {
    private String access_token;
    private String refresh_token;
    private AthleteResponse athlete;
    private String expires_at;
    private String expires_in;
    private String token_type;
    public StravaOauthResponse(@JsonProperty("access_token") String access_token,
                               @JsonProperty("refresh_token") String refresh_token,
                               @JsonProperty("athlete") AthleteResponse athlete,
                               @JsonProperty("expires_at") String expires_at,
                               @JsonProperty("expires_in") String expires_in,
                               @JsonProperty("token_type") String token_type) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.athlete = athlete;
        this.expires_at = expires_at;
        this.expires_in = expires_in;
        this.token_type = token_type;
    }
}
