package br.com.jbank.dto;

import br.com.jbank.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasClientesDiferentesDto(String cpfClienteDonoContaQueDinheiroSai,
		double valorTransferencia, TipoConta tipoContaOrigemDinheiro, String agenciaDestinoDinheiro,
		String contaDestinoDinheiro) {

}
