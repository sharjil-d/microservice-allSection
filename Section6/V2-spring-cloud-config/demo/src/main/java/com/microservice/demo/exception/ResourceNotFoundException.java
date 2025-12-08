package com.microservice.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName,String fieldName, String fieldValue) {
        super(String.format("Customer with name %s and value %s not found", fieldName, fieldValue));
    }


}
