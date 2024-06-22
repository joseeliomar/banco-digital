package com.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.enumeration.Banco;
import com.example.exception.ValidacaoException;

@Service
public abstract class DepositoContaCorrenteService {
	protected static final Banco ESSE_MESMO_BANCO = Banco.JBANK;

	/**
	 * Válida o valor do depósito.
	 * 
	 * @param valorDeposito
	 */
	protected void validaValorDeposito(double valorDeposito) {
		if (valorDeposito < 0) {
			throw new ValidacaoException("Não é possível depositar um valor negativo.", HttpStatus.BAD_REQUEST);
		}
	}
}
