package br.com.jbank.dto;

import java.io.Serializable;

public abstract class DadosParaTransferenciaBancaria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected double valorTransferencia;

	public double getValorTransferencia() {
		return valorTransferencia;
	}
}