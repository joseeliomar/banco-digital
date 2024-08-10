package com.example.dto;

import com.example.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasMesmoClienteDto(String cpfCliente, double valorTransferencia,
		TipoConta tipoContaOrigemDinheiro) {

}
