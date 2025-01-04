package br.com.jbank.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContaDigitalPessoaFisicaDTO2Busca(String agencia, String conta, int digitoVerificadorConta, String senha,
		String telefone, String email, Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao,
		String cpf, String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae) {

}
