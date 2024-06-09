package com.be_uterace.controller;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.PaymentService;
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

    @PostMapping("/vnpay")
    public ResponseEntity<ResponseObject> vnPayController(){
        ResponseObject responseObject = paymentService.createOrderVNPAY(10000000,"ahihi","http://localhost:8080");
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

    @GetMapping("/vnpay-query")
    public ResponseEntity<String> VNPAYQueryController() throws IOException, InterruptedException {
        String returnVNPAY = paymentService.queryTransactionVNPAY();
        return ResponseEntity.status(HttpStatus.OK).body(returnVNPAY);
    }


}
