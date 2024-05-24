package com.example.exception;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends ExcecaoComStatusHttp {
	private static final long serialVersionUID = 1L;

	public ValidacaoException(String mensagem, HttpStatus httpStatus) {
		super(mensagem, httpStatus);
	}

}