package br.com.jbank.dto;

import java.io.Serializable;

import br.com.jbank.enumeration.Operacao;

public abstract class DadosParaTransferenciaBancaria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Operacao tipoTransferencia;
	protected double valorTransferencia;

	public Operacao getTipoTransferencia() {
		return tipoTransferencia;
	}

	public double getValorTransferencia() {
		return valorTransferencia;
	}
}
