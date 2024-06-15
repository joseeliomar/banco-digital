package com.example.model;

import com.example.enumeration.TipoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ContaPessoaJuridica extends Conta {

	@Column(length = 14, nullable = false)
	private String cnpj;
	
	public ContaPessoaJuridica() {
	}

	public ContaPessoaJuridica(TipoConta tipoConta, double saldo, String cnpj) {
		super(tipoConta, saldo);
		this.cnpj = cnpj;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}
