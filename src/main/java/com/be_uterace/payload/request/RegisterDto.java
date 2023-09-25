package com.be_uterace.payload.request;

import com.be_uterace.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String image;
    private String type_account;
    private String telNumber;
    private String birthday;
    private String gender;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String recaptcha_token;
}
