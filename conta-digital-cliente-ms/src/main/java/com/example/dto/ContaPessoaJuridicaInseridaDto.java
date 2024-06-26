package com.example.dto;

import com.example.enumeration.TipoConta;

public record ContaPessoaJuridicaInseridaDto(Long id, TipoConta tipoConta, double saldo, String cnpj) {

}
