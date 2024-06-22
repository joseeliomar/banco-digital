package com.example.integrationtests.dto;

import java.time.LocalDate;

public record ContaDigitalPessoaFisicaInsercaoDto(String agencia, String conta, String senha, String telefone,
		String email, String cpf, String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae) {

}
