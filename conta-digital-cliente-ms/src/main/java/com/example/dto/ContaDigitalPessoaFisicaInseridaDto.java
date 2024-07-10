package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.model.ContaDigitalPessoaFisica;

public class ContaDigitalPessoaFisicaInseridaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private int digitoVerificadorConta;
	private String senha;
	private String telefone;
	private String email;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private String nomeCompletoMae;

	public ContaDigitalPessoaFisicaInseridaDto() {
	}

	public ContaDigitalPessoaFisicaInseridaDto(ContaDigitalPessoaFisica contaDigitalPessoaFisica) {
		this.agencia = contaDigitalPessoaFisica.getAgencia();
		this.conta = contaDigitalPessoaFisica.getConta();
		this.digitoVerificadorConta = contaDigitalPessoaFisica.getDigitoVerificadorConta();
		this.senha = contaDigitalPessoaFisica.getSenha();
		this.telefone = contaDigitalPessoaFisica.getTelefone();
		this.email = contaDigitalPessoaFisica.getEmail();
		this.dataHoraCadastro = contaDigitalPessoaFisica.getDataHoraCadastro();
		this.dataHoraAlteracao = contaDigitalPessoaFisica.getDataHoraAlteracao();
		this.cpf = contaDigitalPessoaFisica.getCpf();
		this.nomeCompleto = contaDigitalPessoaFisica.getNomeCompleto();
		this.dataNascimento = contaDigitalPessoaFisica.getDataNascimento();
		this.nomeCompletoMae = contaDigitalPessoaFisica.getNomeCompletoMae();
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

	public String getSenha() {
		return senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
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
		ContaDigitalPessoaFisicaInseridaDto other = (ContaDigitalPessoaFisicaInseridaDto) obj;
		return Objects.equals(cpf, other.cpf);
	}

}
