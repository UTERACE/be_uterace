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
    private String userName;
    private String password;
    private String email;
    private Date createdAt;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String telNum;
    private Double totalDistance;
    private Integer ranking;
    private String status;
    private String linkFb;
    private String homeNumber;
    private Area area;

}
