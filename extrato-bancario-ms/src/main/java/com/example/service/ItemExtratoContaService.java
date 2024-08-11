package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.openfeign.feignclient.ContaDigitalClienteMsFeignClient;

public abstract class ItemExtratoContaService {
	
	@Autowired
	protected ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;

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
			throw new ValidacaoException("A descrição da operação não foi informada.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaBancoDestino(Banco bancoDestino) {
		if (bancoDestino == null) {
			throw new ValidacaoException("O banco de destino não foi informado.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaAgenciaDestino(String agenciaDestino) {
		if (agenciaDestino == null || agenciaDestino.isBlank()) {
			throw new ValidacaoException("A agência de destino não foi informada.", HttpStatus.BAD_REQUEST);
		}
		if (agenciaDestino.length() < 10) {
			throw new ValidacaoException("A agência de destino está com menos de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (agenciaDestino.length() > 10) {
			throw new ValidacaoException("A agência de destino está com mais de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaContaDestino(String contaDestino) {
		if (contaDestino == null || contaDestino.isBlank()) {
			throw new ValidacaoException("A conta de destino não foi informada.", HttpStatus.BAD_REQUEST);
		}
		if (contaDestino.length() < 10) {
			throw new ValidacaoException("A conta de destino está com menos de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (contaDestino.length() > 10) {
			throw new ValidacaoException("A conta de destino está com mais de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
}
