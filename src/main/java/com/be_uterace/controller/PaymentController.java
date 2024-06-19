package com.be_uterace.controller;

import com.be_uterace.entity.Payment;
import com.be_uterace.payload.momo.MomoPostINP;
import com.be_uterace.payload.momo.MomoResponseCreate;
import com.be_uterace.payload.momo.MomoResponseINP;
import com.be_uterace.payload.request.PaymentCreateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.vnpay.VnPayCreateDto;
import com.be_uterace.repository.PaymentRepository;
import com.be_uterace.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/momo")
    public ResponseEntity<ResponseObject> momoController(@RequestBody PaymentCreateDto paymentCreateDto, HttpServletRequest request) throws IOException, InterruptedException {
        ResponseObject responseObject = paymentService.createOrderMOMO(paymentCreateDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PostMapping("/vnpay")
    public ResponseEntity<ResponseObject> vnPayController(@RequestBody PaymentCreateDto paymentCreateDto, HttpServletRequest request){
        ResponseObject responseObject = paymentService.createOrderVNPAY(paymentCreateDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

//    @GetMapping("/vnpay-payment")
//    public ResponseEntity<PaymentReturnVNPAY> paymentReturnVNPAYController(HttpServletRequest request){
//        PaymentReturnVNPAY returnVNPAY = paymentService.paymentReturnVNPAY(request);
//        return ResponseEntity.status(HttpStatus.OK).body(returnVNPAY);
//    }
    @GetMapping("/vnpay-payment")
    public void checkPaymentController(HttpServletRequest request) {
        System.out.println("goi ne");

        paymentService.updatePaymentVNPay(request);
    }

    @PostMapping("/momo-payment")
    public ResponseEntity<MomoResponseINP> checkParamsMomo(@RequestBody MomoPostINP momoPostINP) {
        MomoResponseINP momoResponseINP = paymentService.updatePaymentMomo(momoPostINP);
        return ResponseEntity.status(HttpStatus.OK).body(momoResponseINP);
    }

    @GetMapping("/vnpay-query")
    public ResponseEntity<Payment> VNPAYQueryController() throws IOException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentRepository.findByvnpTxnRef("0f94581b50cc40ddbc54dae0d5bff7e3"));
    }


}
