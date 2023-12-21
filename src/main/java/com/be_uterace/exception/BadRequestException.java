package com.be_uterace.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BadRequestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
