package com.example.dto;

import java.util.List;

public record ExtratoContaCorrenteDto(String nome, String cpfCliente, String agencia, String conta, double totalEntradas,
		double totalSaidas, List<MovimentacaoDto> movimentacoes) {

}
