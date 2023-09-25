package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ThirdPartyResponse {
    private String id;
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private List<Map<String, Object>> roles;
}
