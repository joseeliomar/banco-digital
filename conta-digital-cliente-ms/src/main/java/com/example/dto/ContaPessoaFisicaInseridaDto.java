package com.example.dto;

import com.example.enumeration.TipoConta;

public record ContaPessoaFisicaInseridaDto(Long id, TipoConta tipoConta, double saldo, String cpf) {

}
