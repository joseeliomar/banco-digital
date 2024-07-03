package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;

public class ContaPessoaJuridicaInsercaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private TipoConta tipoConta;
	private String cnpj;

	public ContaPessoaJuridicaInsercaoDto() {
	}

	public ContaPessoaJuridicaInsercaoDto(TipoConta tipoConta, String cnpj) {
		this.tipoConta = tipoConta;
		this.cnpj = cnpj;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cnpj, tipoConta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPessoaJuridicaInsercaoDto other = (ContaPessoaJuridicaInsercaoDto) obj;
		return Objects.equals(cnpj, other.cnpj) && tipoConta == other.tipoConta;
	}

}
