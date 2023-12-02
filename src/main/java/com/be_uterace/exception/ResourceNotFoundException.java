package com.be_uterace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'",resourceName, fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public String getResourceName() {
        return resourceName;
    }
    public String getFieldValue() {
        return fieldValue;
    }
}
