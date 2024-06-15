package com.be_uterace.payload.vnpay;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VnPayCreateDto {
    private Integer event_id;
    private String redirect_url;
}
