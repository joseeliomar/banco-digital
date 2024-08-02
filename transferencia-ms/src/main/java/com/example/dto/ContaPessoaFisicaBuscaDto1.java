package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;

public class ContaPessoaFisicaBuscaDto1 implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoConta tipoConta;
	private double saldo;
	private String cpf;

	public ContaPessoaFisicaBuscaDto1() {
	}

	public ContaPessoaFisicaBuscaDto1(Long id, TipoConta tipoConta, double saldo, String cpf) {
		super();
		this.id = id;
		this.tipoConta = tipoConta;
		this.saldo = saldo;
		this.cpf = cpf;
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
		ContaPessoaFisicaBuscaDto1 other = (ContaPessoaFisicaBuscaDto1) obj;
		return Objects.equals(id, other.id);
	}

}
