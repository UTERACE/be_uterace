package com.be_uterace.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
    private String firstname = "";
    private String lastname = "";
    private String email = "";
    private String telNumber = "";
    private String birthday = "";
    private String gender = "";
    private String address = "";
    private String province = "";
    private String district = "";
    private String ward = "";
    private String image = "";
}
