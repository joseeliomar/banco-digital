package com.example.dto;

import com.example.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasClientesDiferentesDto(String cpfClienteDonoContaQueDinheiroSai,
		double valorTransferencia, TipoConta contaOrigemDinheiro, String cpfClienteDonoContaQueDinheiroEntra) {

}
