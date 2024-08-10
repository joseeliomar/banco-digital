package com.example.dto;

import com.example.enumeration.Banco;

public record DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira(Banco bancoOrigemDinheiro,
		String agenciaOrigemDinheiro, String contaOrigemDinheiro, double valorTransferencia,
		String agenciaDestinoDinheiro, String contaDestinoDinheiro) {

}
