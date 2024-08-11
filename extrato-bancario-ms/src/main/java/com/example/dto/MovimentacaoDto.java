package com.example.dto;

import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;

public record MovimentacaoDto(String dia, String horario, Banco banco, String agenciaDestino, String contaDestino,
		Operacao operacaoEfetuada, String descricaoOperacao, double valor) {

}
