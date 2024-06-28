package com.be_uterace.service;

import com.be_uterace.entity.*;
import com.be_uterace.entity.enumeration.EPaymentStatus;
import com.be_uterace.exception.BadRequestException;
import com.be_uterace.payload.momo.MomoCreateVm;
import com.be_uterace.payload.momo.MomoPostINP;
import com.be_uterace.payload.momo.MomoResponseCreate;
import com.be_uterace.payload.momo.MomoResponseINP;
import com.be_uterace.payload.request.PaymentCreateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.vnpay.PaymentReturnVNPAY;
import com.be_uterace.payload.vnpay.VnPayCreateDto;
import com.be_uterace.repository.EventRepository;
import com.be_uterace.repository.PaymentProviderRepository;
import com.be_uterace.repository.PaymentRepository;
import com.be_uterace.repository.ShirtSizeRepository;
import com.be_uterace.utils.MomoEncoder;
import com.be_uterace.utils.VNPay;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final MomoEncoder momoEncoder;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final ShirtSizeRepository shirtSizeRepository;
    private final PaymentProviderRepository paymentProviderRepository;
    private final EventService eventService;


    public ResponseObject createOrderMOMO(PaymentCreateDto paymentCreateDto, HttpServletRequest servletRequest) throws IOException, InterruptedException {
        Event event = eventRepository.findEventByEventId(paymentCreateDto.getEvent_id());
        ShirtSize shirtSize = shirtSizeRepository.getReferenceById(paymentCreateDto.getSize_id());
        Optional<PaymentProvider> paymentProviderOptional = paymentProviderRepository.findById(1);
        PaymentProvider paymentProvider = paymentProviderOptional.get();
        if(event.getIsFree()){
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "This event is free of charge.");
        }
        if(shirtSize.getSizeId()==null){
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Shirt Size not found");
        }
        String idrandom = MomoEncoder.generateRequestId();

        MomoCreateVm momoRequest = new MomoCreateVm();
        momoRequest.setPartnerCode(momoEncoder.getPartnerCode());
        momoRequest.setRequestId(idrandom);
        momoRequest.setAmount(event.getRegistrationFee());
        momoRequest.setOrderId(idrandom);
        momoRequest.setOrderInfo("testthanhtoanmomo");
        momoRequest.setRedirectUrl(paymentCreateDto.getRedirect_url());
        momoRequest.setIpnUrl("https://be-uterace-ltf2.onrender.com/api/payment/momo-payment");
        momoRequest.setRequestType("captureWallet");
        momoRequest.setExtraData("eyJ1c2VybmFtZSI6ICJtb21vIn0");
        momoRequest.setLang("vi");

        String decode = "accessKey=" + momoEncoder.getAccessKey()
                + "&amount=" + momoRequest.getAmount()
                + "&extraData=" + momoRequest.getExtraData()
                + "&ipnUrl=" + momoRequest.getIpnUrl()
                + "&orderId=" + momoRequest.getOrderId()
                + "&orderInfo=" + momoRequest.getOrderInfo()
                + "&partnerCode=" + momoRequest.getPartnerCode()
                + "&redirectUrl=" + momoRequest.getRedirectUrl()
                + "&requestId=" + momoRequest.getRequestId()
                + "&requestType=" + momoRequest.getRequestType();

        String signature = momoEncoder.encode(decode);
        momoRequest.setSignature(signature);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(momoRequest);
        System.out.println("JSON Request: " + jsonRequest);

        // Create HttpClient and HttpRequest
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://test-payment.momo.vn/v2/gateway/api/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        MomoResponseCreate momoResponse = objectMapper.readValue(response.body(), MomoResponseCreate.class);
        createPayment(paymentCreateDto,event,shirtSize,paymentProvider,null, idrandom);
        return new ResponseObject(200,momoResponse.getPayUrl());
    }

    public ResponseObject createOrderVNPAY(PaymentCreateDto paymentCreateDto, HttpServletRequest servletRequest){

        Event event = eventRepository.findEventByEventId(paymentCreateDto.getEvent_id());
        ShirtSize shirtSize = shirtSizeRepository.getReferenceById(paymentCreateDto.getSize_id());
        Optional<PaymentProvider> paymentProviderOptional = paymentProviderRepository.findById(2);
        PaymentProvider paymentProvider = paymentProviderOptional.get();
        if(event.getIsFree()){
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "This event is free of charge.");
        }

        if(shirtSize.getSizeId()==null){
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Shirt Size not found");
        }
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPay.generateRequestId();
        String vnp_IpAddr = VNPay.getIpAddress(servletRequest);
        String vnp_TmnCode = VNPay.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(event.getRegistrationFee()*100));
        vnp_Params.put("vnp_CurrCode", "VND");

        System.out.println("vnp_TxnRef: "+vnp_TxnRef);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "orderInfor");
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        //urlReturn += VNPay.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", paymentCreateDto.getRedirect_url());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 1000);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);


//        cld.setTimeZone(TimeZone.getTimeZone("UTC"));
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        // Định dạng lại thời gian trong múi giờ UTC
//        String utc_CreateDate = formatter.format(cld.getTime());
//        System.out.println("vnp_CreateDate (UTC): " + utc_CreateDate);
//        vnp_Params.put("vnp_CreateDate", utc_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String utc_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", utc_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPay.hmacSHA512(VNPay.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        createPayment(paymentCreateDto,event,shirtSize,paymentProvider, vnp_TxnRef,null);
        return new ResponseObject(200,VNPay.vnp_PayUrl + "?" + queryUrl);
    }

    public MomoResponseINP updatePaymentMomo(MomoPostINP momoPostINP){
        Payment payment = paymentRepository.findByRequestIdMomo(momoPostINP.getRequestId());
        if(momoPostINP.getResultCode()==0){
            payment.setPaymentStatus(EPaymentStatus.COMPLETED);
            eventService.joinEvent(payment.getEvent().getEventId(),payment.getUser());
        }else{
            payment.setPaymentStatus(EPaymentStatus.CANCELLED);
        }

        String decode = "accessKey=" + momoEncoder.getAccessKey()
                + "&extraData=" + momoPostINP.getExtraData()
                + "&message=" + momoPostINP.getMessage()
                + "&partnerCode=" + momoEncoder.getPartnerCode()
                + "&requestId=" + momoPostINP.getRequestId()
                + "&responseTime=" + momoPostINP.getResponseTime()
                + "&resultCode=" + momoPostINP.getResultCode();
        String signature = momoEncoder.encode(decode);

        paymentRepository.save(payment);
        MomoResponseINP momoResponseINP = new MomoResponseINP();
        momoResponseINP.setPartnerCode(momoEncoder.getPartnerCode());
        momoResponseINP.setOrderId(momoPostINP.getOrderId());
        momoResponseINP.setResponseTime(momoPostINP.getResponseTime());
        momoResponseINP.setResultCode(0);
        momoResponseINP.setExtraData("");
        momoResponseINP.setMessage("Thanh cong");
        momoResponseINP.setSignature(signature);
        return momoResponseINP;
    }

    public void updatePaymentVNPay(HttpServletRequest request){
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        System.out.println("updatePaymentVNPay:" + vnpTxnRef);
        Payment payment = paymentRepository.findByvnpTxnRef(vnpTxnRef);
        if(checkStatusPaymentVNPAY(request)==1){
            payment.setPaymentStatus(EPaymentStatus.COMPLETED);
            payment.setTransactionId(Long.parseLong(request.getParameter("vnp_TransactionNo")));
            eventService.joinEvent(payment.getEvent().getEventId(),payment.getUser());
        }else{
            payment.setPaymentStatus(EPaymentStatus.CANCELLED);
        }
        paymentRepository.save(payment);
    }


    public int checkStatusPaymentMomo(MomoPostINP momoPostINP){

        String decode = "accessKey=" + momoEncoder.getAccessKey()
                + "&amount=" + momoPostINP.getAmount()
                + "&extraData=" + momoPostINP.getExtraData()
                + "&message=" + momoPostINP.getMessage()
                + "&orderId=" + momoPostINP.getOrderId()
                + "&orderInfo=" + momoPostINP.getOrderInfo()
                + "&orderType=" + momoPostINP.getOrderType()
                + "&partnerCode=" + momoEncoder.getPartnerCode()
                + "&payType=" + momoPostINP.getPayType()
                + "&requestId=" + momoPostINP.getRequestId()
                + "&responseTime=" + momoPostINP.getResponseTime()
                + "&resultCode=" + momoPostINP.getResultCode()
                + "&transId=" + momoPostINP.getResultCode();

        String signature = momoEncoder.encode(decode);
        if (signature.equals(momoPostINP.getSignature())) {
            if (momoPostINP.getResultCode()==0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public int checkStatusPaymentVNPAY(HttpServletRequest request){
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPay.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

//    public PaymentReturnVNPAY paymentReturnVNPAY (HttpServletRequest request){
////        System.out.println("requet   :"+request.getParameter());
//        int paymentStatus = orderReturnVNPAY(request);
//        PaymentReturnVNPAY paymentReturnVNPAY = new PaymentReturnVNPAY();
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//        if(paymentStatus==1){
//            paymentReturnVNPAY.builder()
//                    .orderInfo(orderInfo)
//                    .paymentTime(paymentTime)
//                    .transactionId(transactionId)
//                    .totalPrice(totalPrice)
//                    .build();
//            return paymentReturnVNPAY;
//        }
//        return paymentReturnVNPAY;
//    }

    public String queryTransactionVNPAY() throws IOException, InterruptedException, IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TxnRef = VNPay.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_RequestId", VNPay.generateRequestId());
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", "45328200");
        vnp_Params.put("vnp_OrderInfo", "ahihi");
        vnp_Params.put("vnp_TransactionNo", "14451074");
        vnp_Params.put("vnp_TransactionDate", "20240610001641");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String data = vnp_Params.get("vnp_RequestId") + "|" +
                vnp_Params.get("vnp_Version") + "|" +
                vnp_Params.get("vnp_Command") + "|" +
                vnp_Params.get("vnp_TmnCode") + "|" +
                vnp_Params.get("vnp_TxnRef") + "|" +
                vnp_Params.get("vnp_TransactionDate") + "|" +
                vnp_Params.get("vnp_CreateDate") + "|" +
                vnp_Params.get("vnp_IpAddr") + "|" +
                vnp_Params.get("vnp_OrderInfo");

        // Calculate checksum
        vnp_Params.put("vnp_SecureHash", VNPay.hmacSHA512(VNPay.vnp_HashSecret, data));

        // Convert vnp_Params to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(vnp_Params);

        // Create HttpClient and HttpRequest
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sandbox.vnpayment.vn/merchant_webapi/api/transaction"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print response
        System.out.println(response.statusCode());
        System.out.println(response.body());
        return response.body().toString();
    }

    private void createPayment(PaymentCreateDto paymentCreateDto, Event event,ShirtSize shirtSize, PaymentProvider paymentProvider, String vnp_TxnRef, String request_id){

        User userCurrent = userService.getCurrentLogin();
        Payment payment = new Payment();
        payment.setEvent(event);
        payment.setShirtSize(shirtSize);
        payment.setEmail(paymentCreateDto.getEmail());
        payment.setAddress(paymentCreateDto.getAddress());
        payment.setPaymentStatus(EPaymentStatus.PENDING);
        payment.setUser(userCurrent);
        payment.setPhoneNumber(paymentCreateDto.getPhone_number());
        payment.setPaymentProvider(paymentProvider);
        payment.setVnpTxnRef(vnp_TxnRef);
        payment.setRequestId(request_id);
        paymentRepository.save(payment);
    }
}
