package com.be_uterace.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    @NotBlank(message = "The username is required.")
    private String username;
    @NotBlank(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private String email;
}
