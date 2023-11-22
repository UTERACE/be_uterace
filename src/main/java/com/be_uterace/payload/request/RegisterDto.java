package com.be_uterace.payload.request;

import com.be_uterace.entity.Area;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "The username is required.")
    @Size(min = 3, max = 20, message = "The username must be from 3 to 20 characters.")
    private String username;

    @NotBlank(message = "The password is required.")
    private String password;

    private String firstname;
    private String lastname;

    @NotBlank(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
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
