package com.example.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.model.ContaDigitalPessoaFisica;

public class ContaDigitalPessoaFisicaAlteradaDto {

	private String agencia;
	private String conta;
	private String senha;
	private String telefone;
	private String email;
	private Long idEndereco;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private String nomeCompletoMae;
	
	public ContaDigitalPessoaFisicaAlteradaDto() {}

	public ContaDigitalPessoaFisicaAlteradaDto(ContaDigitalPessoaFisica contaDigitalPessoaFisica) {
		this.agencia = contaDigitalPessoaFisica.getAgencia();
		this.conta = contaDigitalPessoaFisica.getConta();
		this.senha = contaDigitalPessoaFisica.getSenha();
		this.telefone = contaDigitalPessoaFisica.getTelefone();
		this.email = contaDigitalPessoaFisica.getEmail();
		this.idEndereco = contaDigitalPessoaFisica.getIdEndereco();
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

	public String getSenha() {
		return senha;
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
		ContaDigitalPessoaFisicaAlteradaDto other = (ContaDigitalPessoaFisicaAlteradaDto) obj;
		return Objects.equals(cpf, other.cpf);
	}

}
