package com.example.openfeign.exception;

import org.springframework.http.HttpStatus;

public class OpenFeignBadRequestException extends OpenFeignStatusHttpException {
	private static final long serialVersionUID = 1L;

	public OpenFeignBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
