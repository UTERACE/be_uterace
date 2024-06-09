package com.be_uterace.payload.momo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoEncode {
    private String accessKey;
    private int amount;
    private String extraData;
    private String ipnUrl;
    private String orderId;
    private String orderInfo;
    private String partnerCode;
    private String redirectUrl;
    private String requestId;
    private String requestType;
}
