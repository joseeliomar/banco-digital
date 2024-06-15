package com.example.service;

import org.springframework.http.HttpStatus;

import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;

public abstract class ItemExtratoContaService {

	protected void validaTipoConta(TipoConta tipoConta) {
		if (tipoConta == null) {
			throw new ValidacaoException("O tipo de conta não foi informado.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaOperacaoEfetuada(Operacao operacao) {
		if (operacao == null) {
			throw new ValidacaoException("A operação efetuada não foi informada.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaDescricaoOperacao(String descricaoOperacao) {
		if (descricaoOperacao == null || descricaoOperacao.isBlank()) {
			throw new ValidacaoException("A descrição da operação foi não informada.", HttpStatus.BAD_REQUEST);
		}
	}
}
