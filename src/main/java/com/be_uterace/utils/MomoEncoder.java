package com.be_uterace.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;

public class MomoEncoder {

    @Value("${momo.partner-code}")
    private String partnerCode;
    @Value("${momo.access-key}")
    private String accessKey;
    @Value("${momo.secret-key}")
    private String secretKey;

    public static String encode(String key, String data) {
        try {
            byte[] keySect = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa".getBytes();
            HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, keySect);
            //hm256 object can be used again and again
            String hmac = hm256.hmacHex(data);
            return hmac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
