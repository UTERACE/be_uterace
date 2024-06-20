package com.be_uterace.payload.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateResponse {
    private int status;
    private String message;
    private String image;
}
