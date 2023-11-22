package com.be_uterace.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "The username is required.")
    private String username;
    @NotBlank(message = "The password is required.")
    private String password;
}
