package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FacebookResponse {
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private Picture picture;
    public FacebookResponse(@JsonProperty("id") String id,
                            @JsonProperty("email") String email,
                            @JsonProperty("first_name") String first_name,
                            @JsonProperty("last_name") String last_name,
                            @JsonProperty("picture") Picture picture) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.picture = picture;
    }
    public String getPictureUrl() {
        if (picture != null && picture.getData() != null) {
            return picture.getData().getUrl();
        }
        return null;
    }
}
