package com.example.integrationtests.dto;

import com.example.enumeration.TipoConta;

public record DadosContaPessoaFisica(Long idConta, String cpf, TipoConta tipoConta) {

}
