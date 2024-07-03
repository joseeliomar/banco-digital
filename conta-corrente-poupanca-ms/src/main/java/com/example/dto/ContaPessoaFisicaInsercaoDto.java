package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;

public class ContaPessoaFisicaInsercaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private TipoConta tipoConta;
	private String cpf;

	public ContaPessoaFisicaInsercaoDto(TipoConta tipoConta, String cpf) {
		this.tipoConta = tipoConta;
		this.cpf = cpf;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, tipoConta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPessoaFisicaInsercaoDto other = (ContaPessoaFisicaInsercaoDto) obj;
		return Objects.equals(cpf, other.cpf) && tipoConta == other.tipoConta;
	}

}
