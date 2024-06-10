package com.be_uterace.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MomoEncoder {

    @Value("${momo.partner-code}")
    private String partnerCode;
    @Value("${momo.access-key}")
    private String accessKey;
    @Value("${momo.secret-key}")
    private String secretKey;

    public String getPartnerCode() {
        return partnerCode;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String encode(String data) {
        try {
            byte[] keySect = secretKey.getBytes();
            HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, keySect);
            //hm256 object can be used again and again
            String hmac = hm256.hmacHex(data);
            return hmac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
