package com.example.exception;

import org.springframework.http.HttpStatus;

public class ExcecaoComStatusHttp extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected HttpStatus httpStatus;

	public ExcecaoComStatusHttp(String mensagem, HttpStatus httpStatus) {
		super(mensagem);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}