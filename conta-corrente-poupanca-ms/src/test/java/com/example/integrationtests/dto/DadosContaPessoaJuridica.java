package com.example.integrationtests.dto;

import com.example.enumeration.TipoConta;

public record DadosContaPessoaJuridica(Long idConta, String cnpj, TipoConta tipoConta) {

}
