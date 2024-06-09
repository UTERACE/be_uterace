package com.be_uterace.payload.vnpay;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReturnVNPAY {
    private String orderInfo;
    private String paymentTime;
    private String transactionId;
    private String totalPrice;
}
