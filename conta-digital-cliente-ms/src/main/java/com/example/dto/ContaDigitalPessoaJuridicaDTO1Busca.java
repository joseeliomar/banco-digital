package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.model.ContaDigitalPessoaJuridica;

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

	public ContaDigitalPessoaJuridicaDTO1Busca() {
	}

	public ContaDigitalPessoaJuridicaDTO1Busca(ContaDigitalPessoaJuridica contaDigitalPessoaJuridica) {
		this.agencia = contaDigitalPessoaJuridica.getAgencia();
		this.conta = contaDigitalPessoaJuridica.getConta();
		this.digitoVerificadorConta = contaDigitalPessoaJuridica.getDigitoVerificadorConta();
		this.telefone = contaDigitalPessoaJuridica.getTelefone();
		this.email = contaDigitalPessoaJuridica.getEmail();
		this.idEndereco = contaDigitalPessoaJuridica.getIdEndereco();
		this.dataHoraCadastro = contaDigitalPessoaJuridica.getDataHoraCadastro();
		this.dataHoraAlteracao = contaDigitalPessoaJuridica.getDataHoraAlteracao();
		this.cnpj = contaDigitalPessoaJuridica.getCnpj();
		this.razaoSocial = contaDigitalPessoaJuridica.getRazaoSocial();
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
