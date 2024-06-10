package com.be_uterace.service;

import com.be_uterace.payload.momo.MomoCreateVm;
import com.be_uterace.payload.momo.MomoResponseCreate;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.vnpay.PaymentReturnVNPAY;
import com.be_uterace.utils.MomoEncoder;
import com.be_uterace.utils.VNPay;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public MomoResponseCreate createOrderMOMO(Integer user_id) throws IOException, InterruptedException {
        String idrandom = MomoEncoder.generateRequestId();

        MomoCreateVm momoRequest = new MomoCreateVm();
        momoRequest.setPartnerCode(momoEncoder.getPartnerCode());
        momoRequest.setRequestId(idrandom);
        momoRequest.setAmount(10000);
        momoRequest.setOrderId(idrandom);
        momoRequest.setOrderInfo("testthanhtoanmomo");
        momoRequest.setRedirectUrl("http://localhost:8080/Shopee/pay/result");
        momoRequest.setIpnUrl("http://localhost:8080/Shopee/pay/result");
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
        return momoResponse;
    }

    public ResponseObject createOrderVNPAY(int total, String orderInfor, String urlReturn){
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPay.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPay.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total*100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += VNPay.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        System.out.println("vnp_CreateDate  :"+vnp_CreateDate );

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

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
        return new ResponseObject(200,VNPay.vnp_PayUrl + "?" + queryUrl);
    }

    public int orderReturnVNPAY(HttpServletRequest request){
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

    public PaymentReturnVNPAY paymentReturnVNPAY (HttpServletRequest request){
//        System.out.println("requet   :"+request.getParameter());
        int paymentStatus = orderReturnVNPAY(request);
        PaymentReturnVNPAY paymentReturnVNPAY = new PaymentReturnVNPAY();
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        if(paymentStatus==1){
            paymentReturnVNPAY.builder()
                    .orderInfo(orderInfo)
                    .paymentTime(paymentTime)
                    .transactionId(transactionId)
                    .totalPrice(totalPrice)
                    .build();
            return paymentReturnVNPAY;
        }
        return paymentReturnVNPAY;
    }

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
}
