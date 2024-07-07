package com.be_uterace.payload.response;

import com.be_uterace.entity.enumeration.EPaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ManagePaymentSearchResponse {
    private int per_page;

    private int current_page;

    private int total_page;

    private int total_payments;

    private List<ManagePaymentSearchResponse.PaymentItem> payment;

    // getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentItem {
        private Long payment_id;

        private Long transaction_id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String vnpTxnRef;

        private  Integer event_id;

        private Long user_id;

        private EPaymentStatus payment_status;

        private String createdOn;
    }
}
