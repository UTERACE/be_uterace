package com.be_uterace.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConnectStravaResponse {
    private String detail;
    private Long stravaId;
    private String stravaFullname;
    private String stravaImage;
}
