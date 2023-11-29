package com.be_uterace.payload.request;

import com.be_uterace.enums.user.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String address = "";
    private String province = "";
    private String district = "";
    private String ward = "";
    private String image = "";
}
