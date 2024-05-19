package com.example.services.controller.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.model.ContaDigitalPessoaFisica;

public class ContaDigitalPessoaFisicaDTO1Busca implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String agencia;
	private String conta;
	private String telefone;
	private String email;
	private Long idEndereco;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private String nomeCompletoMae;

	public ContaDigitalPessoaFisicaDTO1Busca(ContaDigitalPessoaFisica contaDigitalPessoaFisica) {
		agencia = contaDigitalPessoaFisica.getAgencia();
		conta = contaDigitalPessoaFisica.getConta();
		telefone = contaDigitalPessoaFisica.getTelefone();
		email = contaDigitalPessoaFisica.getEmail();
		idEndereco = contaDigitalPessoaFisica.getIdEndereco();
		dataHoraCadastro = contaDigitalPessoaFisica.getDataHoraCadastro();
		dataHoraAlteracao = contaDigitalPessoaFisica.getDataHoraAlteracao();
		cpf = contaDigitalPessoaFisica.getCpf();
		nomeCompleto = contaDigitalPessoaFisica.getNomeCompleto();
		dataNascimento = contaDigitalPessoaFisica.getDataNascimento();
		nomeCompletoMae = contaDigitalPessoaFisica.getNomeCompletoMae();
	}

	public String getAgencia() {
		return agencia;
	}

	public String getConta() {
		return conta;
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
