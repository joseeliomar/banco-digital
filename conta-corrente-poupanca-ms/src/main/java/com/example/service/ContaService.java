package com.example.service;

import org.springframework.http.HttpStatus;

import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;

public abstract class ContaService {

	protected void validaTipoConta(TipoConta tipoConta) {
		if (tipoConta == null) {
			throw new ValidacaoException("O tipo de conta não foi informado.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaSaldo(double saldo) {
		if (saldo != 0) {
			throw new ValidacaoException("O saldo inicial deve ser zero.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaIdConta(Long idConta) {
		if (idConta == null) {
			throw new ValidacaoException("O código da conta não foi informado.", HttpStatus.BAD_REQUEST);
		}
	}
}
