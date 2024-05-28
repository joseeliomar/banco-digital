package com.example.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
abstract class ContaDigitalCliente {

	@Column(nullable = false, length = 10)
	private String agencia;

	@Column(nullable = false, length = 10)
	private String conta;

	@Column(nullable = false, length = 16)
	private String senha;

	@Column(nullable = false, length = 11)
	private String telefone;

	@Column(nullable = false, length = 50)
	private String email;

	@Column(nullable = true)
	private Long idEndereco;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraCadastro;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraAlteracao;
	
	public ContaDigitalCliente() {
	}

	public ContaDigitalCliente(String agencia, String conta, String senha, String telefone, String email,
			Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao) {
		this.agencia = agencia;
		this.conta = conta;
		this.senha = senha;
		this.telefone = telefone;
		this.email = email;
		this.idEndereco = idEndereco;
		this.dataHoraCadastro = dataHoraCadastro;
		this.dataHoraAlteracao = dataHoraAlteracao;
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

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public void setDataHoraCadastro(LocalDateTime dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}

}
