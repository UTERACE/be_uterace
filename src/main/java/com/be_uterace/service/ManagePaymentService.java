package com.be_uterace.service;

import com.be_uterace.payload.response.ManagePaymentSearchResponse;
import com.be_uterace.payload.response.ManagePostSearchResponse;

public interface ManagePaymentService {

    ManagePaymentSearchResponse searchVNPayPayment(int current_page, int per_page);

    ManagePaymentSearchResponse searchMomoPayment(int current_page, int per_page, String search);

}
