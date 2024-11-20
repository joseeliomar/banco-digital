package br.com.jbank.dto;

import br.com.jbank.enumeration.Operacao;

public record TransferenciaEntreContasMesmoClienteDto(String cpfCliente, Operacao tipoTransferencia,
		double valorTransferencia, String tipoContaOrigemDinheiro) {

}
