package com.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "contas_digitais_pessoas_fisicas", uniqueConstraints = {
		@UniqueConstraint(name = "unica_agencia_conta_tabela_cdpf", columnNames = { "agencia", "conta" }) })
public class ContaDigitalPessoaFisica extends ContaDigitalCliente {

	@Id
	@Column(length = 11)
	private String cpf;

	@Column(name = "nome_completo", nullable = false, length = 100)
	private String nomeCompleto;

	@Column(name = "data_nascimento", nullable = false)
	@Temporal(TemporalType.DATE)
	private LocalDate dataNascimento;

	@Column(name = "nome_completo_mae", nullable = false, length = 100)
	private String nomeCompletoMae;
	
	public ContaDigitalPessoaFisica() {
		super();
	}

	public ContaDigitalPessoaFisica(String agencia, String conta, String senha, String telefone, String email,
			Long idEndereco, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao, String cpf,
			String nomeCompleto, LocalDate dataNascimento, String nomeCompletoMae, int digitoVerificadorConta) {
		super(agencia, conta, senha, telefone, email, idEndereco, dataHoraCadastro, dataHoraAlteracao,
				digitoVerificadorConta);
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.dataNascimento = dataNascimento;
		this.nomeCompletoMae = nomeCompletoMae;
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
		ContaDigitalPessoaFisica other = (ContaDigitalPessoaFisica) obj;
		return Objects.equals(cpf, other.cpf);
	}
}
