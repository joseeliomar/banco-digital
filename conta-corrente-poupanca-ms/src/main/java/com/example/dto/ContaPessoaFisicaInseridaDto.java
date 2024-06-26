package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaFisica;

public class ContaPessoaFisicaInseridaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoConta tipoConta;
	private double saldo;
	private String cpf;
	
	public ContaPessoaFisicaInseridaDto() {
	}

	public ContaPessoaFisicaInseridaDto(ContaPessoaFisica contaPessoaFisica) {
		id = contaPessoaFisica.getId();
		tipoConta = contaPessoaFisica.getTipoConta();
		saldo = contaPessoaFisica.getSaldo();
		cpf = contaPessoaFisica.getCpf();
	}

	public Long getId() {
		return id;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public double getSaldo() {
		return saldo;
	}

	public String getCpf() {
		return cpf;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPessoaFisicaInseridaDto other = (ContaPessoaFisicaInseridaDto) obj;
		return Objects.equals(id, other.id);
	}

}
