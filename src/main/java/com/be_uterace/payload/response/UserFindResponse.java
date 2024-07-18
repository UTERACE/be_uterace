package com.be_uterace.payload.response;

import com.be_uterace.enums.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFindResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String avatar;
}
