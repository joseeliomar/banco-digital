package com.example.openfeign.exception;

import org.springframework.http.HttpStatus;

public class OpenFeignNotFoundException extends OpenFeignStatusHttpException {
	private static final long serialVersionUID = 1L;

	public OpenFeignNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
