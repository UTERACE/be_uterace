package com.be_uterace.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class PasswordGeneratorEncoder {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
        System.out.println(passwordEncoder.encode("sinhhung"));
        System.out.println(generateRequestId());
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
