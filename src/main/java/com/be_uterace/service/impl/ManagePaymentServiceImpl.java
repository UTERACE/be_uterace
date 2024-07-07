package com.be_uterace.service.impl;

import com.be_uterace.entity.Payment;
import com.be_uterace.payload.response.ManagePaymentSearchResponse;
import com.be_uterace.repository.PaymentRepository;
import com.be_uterace.service.ManagePaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagePaymentServiceImpl implements ManagePaymentService {

    private final PaymentRepository paymentRepository;

    public ManagePaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ManagePaymentSearchResponse searchVNPayPayment(int current_page, int per_page) {
        Page<Payment> paymentsPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        paymentsPage = paymentRepository.findByPaymentProviderId(2,pageable);

        List<Payment> paymentList = paymentsPage.getContent();
        List<ManagePaymentSearchResponse.PaymentItem> paymentItems = new ArrayList<>();
        for (Payment payment : paymentList){
            ManagePaymentSearchResponse.PaymentItem paymentItem = new ManagePaymentSearchResponse.PaymentItem();
            paymentItem.setPayment_id(payment.getPaymentId());
            paymentItem.setUser_id(payment.getUser().getUserId());
            paymentItem.setEvent_id(payment.getEvent().getEventId());
            paymentItem.setVnpTxnRef(payment.getVnpTxnRef());
            paymentItem.setTransaction_id((payment.getTransactionId()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
            String formattedDate = payment.getCreatedOn().format(formatter);
            paymentItem.setCreatedOn(formattedDate);
            paymentItem.setPayment_status(payment.getPaymentStatus());
            paymentItems.add(paymentItem);
        }
        return ManagePaymentSearchResponse.builder()
                .per_page(paymentsPage.getSize())
                .total_payments((int) paymentsPage.getTotalElements())
                .current_page(paymentsPage.getNumber() + 1)
                .total_page(paymentsPage.getTotalPages())
                .payment(paymentItems)
                .build();
    }

    @Override
    public ManagePaymentSearchResponse searchMomoPayment(int current_page, int per_page, String search) {
        Page<Payment> paymentsPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        paymentsPage = paymentRepository.findByPaymentProviderId(1,pageable);

        List<Payment> paymentList = paymentsPage.getContent();
        List<ManagePaymentSearchResponse.PaymentItem> paymentItems = new ArrayList<>();
        for (Payment payment : paymentList){
            ManagePaymentSearchResponse.PaymentItem paymentItem = new ManagePaymentSearchResponse.PaymentItem();
            paymentItem.setPayment_id(payment.getPaymentId());
            paymentItem.setUser_id(payment.getUser().getUserId());
            paymentItem.setEvent_id(payment.getEvent().getEventId());
            paymentItem.setTransaction_id((payment.getTransactionId()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
            String formattedDate = payment.getCreatedOn().format(formatter);
            paymentItem.setCreatedOn(formattedDate);
            paymentItem.setPayment_status(payment.getPaymentStatus());
            paymentItems.add(paymentItem);
        }
        return ManagePaymentSearchResponse.builder()
                .per_page(paymentsPage.getSize())
                .total_payments((int) paymentsPage.getTotalElements())
                .current_page(paymentsPage.getNumber() + 1)
                .total_page(paymentsPage.getTotalPages())
                .payment(paymentItems)
                .build();
    }
}
