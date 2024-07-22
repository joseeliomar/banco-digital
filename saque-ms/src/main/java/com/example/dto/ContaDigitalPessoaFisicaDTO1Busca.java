package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ContaDigitalPessoaFisicaDTO1Busca implements Serializable {

	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private int digitoVerificadorConta;
	private String telefone;
	private String email;
	private Long idEndereco;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private String nomeCompletoMae;

	public ContaDigitalPessoaFisicaDTO1Busca() {
	}

	public ContaDigitalPessoaFisicaDTO1Busca(String agencia, String conta, int digitoVerificadorConta, String telefone,
			String email, Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao, String cpf,
			String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae) {
		this.agencia = agencia;
		this.conta = conta;
		this.digitoVerificadorConta = digitoVerificadorConta;
		this.telefone = telefone;
		this.email = email;
		this.idEndereco = idEndereco;
		this.dataHoraCadastro = dataHoraCadastro;
		this.dataHoraAlteracao = dataHoraAlteracao;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.dataNascimento = dataNascimento;
		this.nomeCompletoMae = nomeCompletoMae;
	}

	public String getAgencia() {
		return agencia;
	}

	public String getConta() {
		return conta;
	}

	public int getDigitoVerificadorConta() {
		return digitoVerificadorConta;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

	public Long getIdEndereco() {
		return idEndereco;
	}

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public String getNomeCompletoMae() {
		return nomeCompletoMae;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaDigitalPessoaFisicaDTO1Busca other = (ContaDigitalPessoaFisicaDTO1Busca) obj;
		return Objects.equals(cpf, other.cpf);
	}

}
