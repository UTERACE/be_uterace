package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GoogleResponse {
    private String id;
    private String email;
    private boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
    @JsonCreator
    public GoogleResponse(@JsonProperty("id") String id,
                          @JsonProperty("email") String email,
                          @JsonProperty("verified_email") boolean verified_email,
                          @JsonProperty("name") String name,
                          @JsonProperty("given_name") String given_name,
                          @JsonProperty("family_name") String family_name,
                          @JsonProperty("picture") String picture,
                          @JsonProperty("locale") String locale) {
        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.locale = locale;
    }
}
