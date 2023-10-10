package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AthleteResponse {
    private Long id;
    private String username;
    private String resource_state;
    private String firstname;
    private String lastname;
    private String city;
    private String state;
    private String country;
    private String sex;
    private String premium;
    private String created_at;
    private String updated_at;
    private String badge_type_id;
    private String profile_medium;
    private String profile;
    private String friend;
    private String follower;
    public AthleteResponse(@JsonProperty("id") Long id,
                           @JsonProperty("username") String username,
                           @JsonProperty("resource_state") String resource_state,
                           @JsonProperty("firstname") String firstname,
                           @JsonProperty("lastname") String lastname,
                           @JsonProperty("city") String city,
                           @JsonProperty("state") String state,
                           @JsonProperty("country") String country,
                           @JsonProperty("sex") String sex,
                           @JsonProperty("premium") String premium,
                           @JsonProperty("created_at") String created_at,
                           @JsonProperty("updated_at") String updated_at,
                           @JsonProperty("badge_type_id") String badge_type_id,
                           @JsonProperty("profile_medium") String profile_medium,
                           @JsonProperty("profile") String profile,
                           @JsonProperty("friend") String friend,
                           @JsonProperty("follower") String follower) {
        this.id = id;
        this.username = username;
        this.resource_state = resource_state;
        this.firstname = firstname;
        this.lastname = lastname;
        this.city = city;
        this.state = state;
        this.country=country;
        this.sex=sex;
        this.premium=premium;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.badge_type_id=badge_type_id;
        this.profile_medium=profile_medium;
        this.profile=profile;
        this.friend=friend;
        this.follower=follower;
    }
    public String getFullName() {
        return firstname + " " + lastname;
    }
}
