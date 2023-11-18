package com.be_uterace.utils;

public class Validation {
    public static String checkPace(double pace) {
        if (pace > 1) {
            return "1";
        } else {
            return "0";
        }
    }
}
