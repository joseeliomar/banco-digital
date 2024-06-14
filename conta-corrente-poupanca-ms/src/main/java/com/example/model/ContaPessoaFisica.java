package com.example.model;

import com.example.enumeration.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ContaPessoaFisica extends Conta {

	@Column(length = 11)
	private String cpf;
	
	public ContaPessoaFisica() {
	}
	
	public ContaPessoaFisica(TipoConta tipoConta, double saldo, String cpf) {
		super(tipoConta, saldo);
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
