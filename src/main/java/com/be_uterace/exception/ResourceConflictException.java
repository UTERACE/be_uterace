package com.be_uterace.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException{
    private String resourceName;

    public ResourceConflictException(String resourceName) {
        super(String.format("%s already exists", resourceName));
        this.resourceName = resourceName;

    }

    public String getResourceName() {
        return resourceName;
    }

}
