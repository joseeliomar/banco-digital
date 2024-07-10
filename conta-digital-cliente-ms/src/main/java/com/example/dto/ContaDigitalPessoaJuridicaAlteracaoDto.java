package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

public class ContaDigitalPessoaJuridicaAlteracaoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String agencia;
	private String senha;
	private String telefone;
	private String email;
	private Long idEndereco;
	private String cnpj;
	private String razaoSocial;

	public ContaDigitalPessoaJuridicaAlteracaoDto(String agencia, String senha, String telefone,
			String email, Long idEndereco, String cnpj, String razaoSocial) {
		this.agencia = agencia;
		this.senha = senha;
		this.telefone = telefone;
		this.email = email;
		this.idEndereco = idEndereco;
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
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
		ContaDigitalPessoaJuridicaAlteracaoDto other = (ContaDigitalPessoaJuridicaAlteracaoDto) obj;
		return Objects.equals(cnpj, other.cnpj);
	}
}
