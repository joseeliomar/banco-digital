package com.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.enumeration.Banco;
import com.example.exception.ValidacaoException;

@Service
public abstract class SaqueContaCorrenteService {
	protected static final Banco ESSE_MESMO_BANCO = Banco.JBANK;

	/**
	 * Válida o valor do saque.
	 * 
	 * @param valorSaque
	 */
	protected void validaValorSaque(double valorSaque) {
		if (valorSaque < 0) {
			throw new ValidacaoException("Não é possível sacar um valor negativo.", HttpStatus.BAD_REQUEST);
		}
	}
}
