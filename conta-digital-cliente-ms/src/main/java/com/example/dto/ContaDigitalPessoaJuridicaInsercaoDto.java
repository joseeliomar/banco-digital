package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

public class ContaDigitalPessoaJuridicaInsercaoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private String senha;
	private String telefone;
	private String email;
	private String cnpj;
	private String razaoSocial;

	public ContaDigitalPessoaJuridicaInsercaoDto(String agencia, String conta, String senha, String telefone,
			String email, String cnpj, String razaoSocial) {
		this.agencia = agencia;
		this.conta = conta;
		this.senha = senha;
		this.telefone = telefone;
		this.email = email;
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cnpj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaDigitalPessoaJuridicaInsercaoDto other = (ContaDigitalPessoaJuridicaInsercaoDto) obj;
		return Objects.equals(cnpj, other.cnpj);
	}

}
