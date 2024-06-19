package com.be_uterace.payload.momo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoResponseINP {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private int resultCode;
    private String message;
    private String responseTime;
    private String extraData;
    private String signature;
}
