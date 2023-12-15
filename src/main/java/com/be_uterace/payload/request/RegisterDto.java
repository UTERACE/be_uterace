package com.be_uterace.payload.request;

import com.be_uterace.entity.Area;
import com.be_uterace.enums.user.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Size(min = 8, max = 50, message = "The username must be between 8 and 50 characters.")
    private String username;

    @NotBlank(message = "The password is required.")
    private String password;
    @NotBlank(message = "The firstname is required.")
    private String firstname;
    @NotBlank(message = "The lastname is required.")
    private String lastname;

    @NotBlank(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private String email;

    private String image;
    private String type_account;
    private String telNumber;
    private String birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;
    private String province;
    private String district;
    private String ward;
    private String recaptcha_token;
}
