package com.example.dto;

import com.example.enumeration.Banco;

public record DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira(Banco bancoOrigemDiheiro,
		String agenciaOrigemDiheiro, String contaOrigemDiheiro, double valorTransferencia, String agenciaDestinoDiheiro,
		String contaDestinoDiheiro) {

}
