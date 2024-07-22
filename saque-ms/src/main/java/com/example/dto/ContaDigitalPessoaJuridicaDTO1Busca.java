package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ContaDigitalPessoaJuridicaDTO1Busca implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String agencia;
	private String conta;
	private int digitoVerificadorConta;
	private String telefone;
	private String email;
	private Long idEndereco;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cnpj;
	private String razaoSocial;
	
	public ContaDigitalPessoaJuridicaDTO1Busca() {}
	
	public ContaDigitalPessoaJuridicaDTO1Busca(String agencia, String conta, int digitoVerificadorConta,
			String telefone, String email, Long idEndereco, LocalDateTime dataHoraCadastro,
			LocalDateTime dataHoraAlteracao, String cnpj, String razaoSocial) {
		this.agencia = agencia;
		this.conta = conta;
		this.digitoVerificadorConta = digitoVerificadorConta;
		this.telefone = telefone;
		this.email = email;
		this.idEndereco = idEndereco;
		this.dataHoraCadastro = dataHoraCadastro;
		this.dataHoraAlteracao = dataHoraAlteracao;
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
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

	public String getCnpj() {
		return cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
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
		ContaDigitalPessoaJuridicaDTO1Busca other = (ContaDigitalPessoaJuridicaDTO1Busca) obj;
		return Objects.equals(cnpj, other.cnpj);
	}

}
