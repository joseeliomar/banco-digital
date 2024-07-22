package com.example.integrationtests.controller;

import java.time.LocalDate;

public record DadosContaClientePessoaFisica(String agencia, String conta, String senha, String telefone,
		String email, String cpfCliente, String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae) {

}
