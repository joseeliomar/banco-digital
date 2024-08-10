package com.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.enumeration.Banco;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.utils.Utils;

@Service
public abstract class TransferenciaComContasService {
	protected static final Banco ESSE_MESMO_BANCO = Banco.JBANK;

	/**
	 * Valida o valor da transferência.
	 * 
	 * @param valorSaque
	 */
	protected void validaValorTransferencia(double valorSaque) {
		if (valorSaque < 0) {
			throw new ValidacaoException("Não é possível transferir um valor negativo.", HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Valida se foi informada a conta da origem do dinheiro.
	 * 
	 * @param dadosParaTransferenciaDto
	 */
	protected void validaContaOrigemDinheiro(TipoConta contaOrigemDinheiro) {
		if (contaOrigemDinheiro == null) {
			throw new ValidacaoException("Não foi informado a conta da origem do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Valida se o CPF foi informado.
	 * 
	 * @param cpfCliente
	 */
	protected void validaCpf(String cpfCliente) {
		if (Utils.stringNulaVaziaOuEmBranco(cpfCliente)) {
			throw new ValidacaoException("O CPF não foi informado.", HttpStatus.BAD_REQUEST);
		}
	}

	protected void validaBancoOrigem(Banco bancoOrigem) {
		if (bancoOrigem == null) {
			throw new ValidacaoException("Não foi informado o banco de origem do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaBancoDestino(Banco bancoDestino) {
		if (bancoDestino == null) {
			throw new ValidacaoException("Não foi informado o banco de destino do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaAgenciaOrigem(String agenciaOrigem) {
		if (Utils.stringNulaVaziaOuEmBranco(agenciaOrigem)) {
			throw new ValidacaoException("Não foi informada a agência de origem do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}

	protected void validaAgenciaDestino(String agenciaDestino) {
		if (Utils.stringNulaVaziaOuEmBranco(agenciaDestino)) {
			throw new ValidacaoException("Não foi informada a agência de destino do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaContaOrigem(String contaOrigem) {
		if (Utils.stringNulaVaziaOuEmBranco(contaOrigem)) {
			throw new ValidacaoException("Não foi informada a conta de origem do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaContaDestino(String contaDestino) {
		if (Utils.stringNulaVaziaOuEmBranco(contaDestino)) {
			throw new ValidacaoException("Não foi informada a conta de destino do dinheiro.", HttpStatus.BAD_REQUEST);
		}
	}
}
