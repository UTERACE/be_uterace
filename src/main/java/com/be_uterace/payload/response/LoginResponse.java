package com.be_uterace.payload.response;


import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private List<Map<String, Object>> roles;
}
