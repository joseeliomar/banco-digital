package br.com.jbank.dto;

import br.com.jbank.enumeration.Banco;
import br.com.jbank.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto(String cpfClienteDonoContaQueDinheiroSai,
		double valorTransferencia, TipoConta tipoContaOrigemDinheiro, Banco bancoDestino, String agenciaDestino, String contaDestino) {

}
