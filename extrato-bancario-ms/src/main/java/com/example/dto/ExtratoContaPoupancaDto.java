package com.example.dto;

import java.util.List;

public record ExtratoContaPoupancaDto(String periodo, String nome, String cpfCliente, String agencia, String conta,
		double totalEntradas, double totalSaidas, double saldoFinalPeriodo, List<MovimentacaoDto> movimentacoes) {

}
