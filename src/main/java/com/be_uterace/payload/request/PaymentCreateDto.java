package com.be_uterace.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateDto {
    private Integer event_id;
    private String redirect_url;
    private Integer size_id;
    private String address;
    private String phone_number;
    private String email;
}
