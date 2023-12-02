package com.be_uterace.exception;

public class ErrorHolder {
    private static final ThreadLocal<String> errorMessageHolder = new ThreadLocal<>();

    public static String getErrorMessage() {
        return errorMessageHolder.get();
    }

    public static void setErrorMessage(String errorMessage) {
        errorMessageHolder.set(errorMessage);
    }

    public static void clearErrorMessage() {
        errorMessageHolder.remove();
    }
}
