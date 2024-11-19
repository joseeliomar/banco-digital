package br.com.jbank.dto;

import br.com.jbank.enumeration.TipoConta;

public record DadosParaTransferenciaEntreContasMesmoClienteDto(String cpfCliente, double valorTransferencia,
		TipoConta tipoContaOrigemDinheiro) {

}
