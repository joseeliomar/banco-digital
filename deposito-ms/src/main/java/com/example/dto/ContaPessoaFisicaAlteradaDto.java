package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.TipoConta;

public class ContaPessoaFisicaAlteradaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private TipoConta tipoConta;
	private double saldo;
	private String cpf;
	
	public ContaPessoaFisicaAlteradaDto() {
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
		ContaPessoaFisicaAlteradaDto other = (ContaPessoaFisicaAlteradaDto) obj;
		return Objects.equals(id, other.id);
	}

}
