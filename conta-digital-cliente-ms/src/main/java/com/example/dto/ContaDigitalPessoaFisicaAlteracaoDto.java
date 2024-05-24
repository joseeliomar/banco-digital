package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ContaDigitalPessoaFisicaAlteracaoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private String senha;
	private String telefone;
	private String email;
	private Long idEndereco;
	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private String nomeCompletoMae;

	public ContaDigitalPessoaFisicaAlteracaoDto(String agencia, String conta, String senha, String telefone,
			String email, Long idEndereco, String cpf,
			String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae) {
		this.agencia = agencia;
		this.conta = conta;
		this.senha = senha;
		this.telefone = telefone;
		this.email = email;
		this.idEndereco = idEndereco;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.dataNascimento = dataNascimento;
		this.nomeCompletoMae = nomeCompletoMae;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(Long idEndereco) {
		this.idEndereco = idEndereco;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNomeCompletoMae() {
		return nomeCompletoMae;
	}

	public void setNomeCompletoMae(String nomeCompletoMae) {
		this.nomeCompletoMae = nomeCompletoMae;
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
		ContaDigitalPessoaFisicaAlteracaoDto other = (ContaDigitalPessoaFisicaAlteracaoDto) obj;
		return Objects.equals(cpf, other.cpf);
	}

}
