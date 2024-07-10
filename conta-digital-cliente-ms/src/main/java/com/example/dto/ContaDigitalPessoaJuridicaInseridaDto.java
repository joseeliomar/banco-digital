package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.model.ContaDigitalPessoaJuridica;

public class ContaDigitalPessoaJuridicaInseridaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private int digitoVerificadorConta;
	private String senha;
	private String telefone;
	private String email;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cnpj;
	private String razaoSocial;

	public ContaDigitalPessoaJuridicaInseridaDto() {
	}

	public ContaDigitalPessoaJuridicaInseridaDto(ContaDigitalPessoaJuridica contaDigitalPessoaJuridica) {
		this.agencia = contaDigitalPessoaJuridica.getAgencia();
		this.conta = contaDigitalPessoaJuridica.getConta();
		this.digitoVerificadorConta = contaDigitalPessoaJuridica.getDigitoVerificadorConta();
		this.senha = contaDigitalPessoaJuridica.getSenha();
		this.telefone = contaDigitalPessoaJuridica.getTelefone();
		this.email = contaDigitalPessoaJuridica.getEmail();
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
		ContaDigitalPessoaJuridicaInseridaDto other = (ContaDigitalPessoaJuridicaInseridaDto) obj;
		return Objects.equals(cnpj, other.cnpj);
	}
}
