package com.be_uterace.payload.momo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoResponseCreate {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private int amount;
    private long responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
    private String applink;
    private String deeplinkMiniApp;
    private String signature;
}
