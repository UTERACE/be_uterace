package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String telNumber;
    private String birthday;
    private String gender;
    private String address;
    private String province;
    private String district;
    private String ward;
    private int org_id;
    private int child_org_id;
    private String image;
}
