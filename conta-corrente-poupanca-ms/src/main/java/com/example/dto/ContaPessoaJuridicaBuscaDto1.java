package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaJuridica;

public class ContaPessoaJuridicaBuscaDto1 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private TipoConta tipoConta;
	private double saldo;
	private String cnpj;

	public ContaPessoaJuridicaBuscaDto1(ContaPessoaJuridica contaPessoaJuridica) {
		id = contaPessoaJuridica.getId();
		tipoConta = contaPessoaJuridica.getTipoConta();
		saldo = contaPessoaJuridica.getSaldo();
		cnpj = contaPessoaJuridica.getCnpj();
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

	public String getCnpj() {
		return cnpj;
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
		ContaPessoaJuridicaBuscaDto1 other = (ContaPessoaJuridicaBuscaDto1) obj;
		return Objects.equals(id, other.id);
	}

}
