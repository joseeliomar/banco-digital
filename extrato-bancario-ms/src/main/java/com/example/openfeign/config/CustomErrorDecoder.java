package com.example.openfeign.config;

import org.springframework.http.HttpStatus;

import com.example.openfeign.exception.OpenFeignBadRequestException;
import com.example.openfeign.exception.OpenFeignNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
    	HttpStatus status = HttpStatus.valueOf(response.status());
        switch (status) {
            case NOT_FOUND:
                return new OpenFeignNotFoundException("Recurso não encontrado");
            case BAD_REQUEST:
                return new OpenFeignBadRequestException("Bad request");
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }

}
