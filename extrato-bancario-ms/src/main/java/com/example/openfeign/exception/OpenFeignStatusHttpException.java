package com.example.openfeign.exception;

import org.springframework.http.HttpStatus;

import com.example.exception.ExcecaoComStatusHttp;

public class OpenFeignStatusHttpException extends ExcecaoComStatusHttp {
	private static final long serialVersionUID = 1L;

	public OpenFeignStatusHttpException(HttpStatus httpStatus, String message) {
        super(message, httpStatus);
    }

}
