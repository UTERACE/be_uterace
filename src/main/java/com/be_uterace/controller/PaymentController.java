package com.be_uterace.controller;

import com.be_uterace.payload.momo.MomoResponseCreate;
import com.be_uterace.payload.request.PaymentCreateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.vnpay.VnPayCreateDto;
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
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
    public String checkParams(@RequestParam Map<String, String> allParams) {
        StringBuilder response = new StringBuilder();
        allParams.forEach((key, value) -> response.append(key).append(" = ").append(value).append("<br>"));
        return response.toString();
    }

    @GetMapping("/momo-payment")
    public String checkParamsMomo(@RequestParam Map<String, String> allParams) {
        StringBuilder response = new StringBuilder();
        allParams.forEach((key, value) -> response.append(key).append(" = ").append(value).append("<br>"));
        return response.toString();
    }

    @GetMapping("/vnpay-query")
    public ResponseEntity<String> VNPAYQueryController() throws IOException, InterruptedException {
        String returnVNPAY = paymentService.queryTransactionVNPAY();
        return ResponseEntity.status(HttpStatus.OK).body(returnVNPAY);
    }


}
