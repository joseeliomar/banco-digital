package com.example.dto;

import com.example.enumeration.Banco;
import com.example.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto(String cpfClienteDonoContaQueDinheiroSai,
		double valorTransferencia, TipoConta tipoContaOrigemDinheiro, Banco bancoDestino, String agenciaDestino, String contaDestino) {

}
