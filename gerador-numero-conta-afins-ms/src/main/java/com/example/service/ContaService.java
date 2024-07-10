package com.example.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.DadosContaDto;
import com.example.utils.SequenceUtils;
import com.example.utils.Utils;

@Service
public class ContaService {

	@Autowired
	private SequenceUtils sequenceUtils;
	
	/**
	 * Gera os dados da conta.
	 * 
	 * @return Os dados da conta (número da conta e o digíto verificador dela).
	 */
	public DadosContaDto geraDadosConta() {
		String numeroContaComZerosEsquerda = geraNumeroConta();
		int digitoVerificadorConta = geraDigitoVerificadorContaComAlgoritmoModulo11(numeroContaComZerosEsquerda);
		
		return new DadosContaDto(numeroContaComZerosEsquerda, digitoVerificadorConta);
	}

	/**
	 * Gera o número da conta com zeros à esquerda.
	 * 
	 * @param nomeSequence
	 * @return O número da conta com zeros à esquerda.
	 */
	private String geraNumeroConta() {
		String nomeSequence = "numero_conta";
		criaSequence(nomeSequence);
		int quantidadeTotalDigitosDesejada = 10;
		return Utils
				.insereZerosEsquerdaNumeroInformado(
						sequenceUtils.obtemProximoValorDaSequence(nomeSequence),
						quantidadeTotalDigitosDesejada);
	}

	/**
	 * Cria uma sequence;
	 * 
	 * @param nomeSequence
	 */
	private void criaSequence(String nomeSequence) {
		if (!sequenceUtils.existeSequence(nomeSequence)) {
			long startWith = 1l, incrementBy = 1l, minValue = 1l, maxValue = 9999999999l, cache = 10l;
			sequenceUtils.criaSequence(nomeSequence, startWith, incrementBy, minValue, maxValue, cache);
		}
	}
	
	/**
	 * Gera o digíto verificador da conta através do algoritmo Módulo 11.
	 * 
	 * @param numeroConta O número da conta
	 * @return O digíto verificador da conta que foi gerado.
	 */
	private int geraDigitoVerificadorContaComAlgoritmoModulo11(String numeroConta) {
		if (Utils.stringNulaVaziaOuEmBranco(numeroConta)) {
			throw new RuntimeException("A string informada está nula, vazia ou só contém espaços em branco.");
		}
		
		if (!StringUtils.isNumeric(numeroConta)) {
			throw new RuntimeException("A string informada não possuí apenas números.");
		}
		
		if (numeroConta.length() != 10) {
			throw new RuntimeException("O número da conta deve ter 10 digítos.");
		}
		
		int soma = 0;
		int[] pesos = { 8, 2, 5, 1, 9, 3, 6, 4, 7, 9};

		for (int i = 0; i < numeroConta.length(); i++) {
			int umDigitoNumeroConta = Character.getNumericValue(numeroConta.charAt(i));
			soma += umDigitoNumeroConta * pesos[i];
		}

		int resto = soma % 11;
		int resultadoSubtracao = 11 - resto;
		int digitoVerificador = (resultadoSubtracao > 9 || resultadoSubtracao == 0) ? 1 : resultadoSubtracao;
		return digitoVerificador;
	}
}
