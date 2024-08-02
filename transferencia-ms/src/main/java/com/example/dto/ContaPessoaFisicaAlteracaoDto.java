package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

public class ContaPessoaFisicaAlteracaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private double saldo;
	
	public ContaPessoaFisicaAlteracaoDto() {
	}

	public ContaPessoaFisicaAlteracaoDto(Long id, double saldo) {
		this.id = id;
		this.saldo = saldo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
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
		ContaPessoaFisicaAlteracaoDto other = (ContaPessoaFisicaAlteracaoDto) obj;
		return Objects.equals(id, other.id);
	}

}
