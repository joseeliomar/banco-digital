package com.example.services.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContaDigitalPessoaFisicaDto(String agencia, String conta, String senha, String telefone, String email,
		Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao, String cpf,
		String nomeCompleto, LocalDate dataNascimento, String paisNascimento, String nomeCompletoMae) {

}
