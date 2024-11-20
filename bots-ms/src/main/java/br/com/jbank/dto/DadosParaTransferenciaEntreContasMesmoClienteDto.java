package br.com.jbank.dto;

import br.com.jbank.enumeration.TipoConta;

public class DadosParaTransferenciaEntreContasMesmoClienteDto extends DadosParaTransferenciaBancaria {
	private static final long serialVersionUID = 1L;

	private String cpfCliente;
	private double valorTransferencia;
	private TipoConta tipoContaOrigemDinheiro;

	public String getCpfCliente() {
		return cpfCliente;
	}

	public double getValorTransferencia() {
		return valorTransferencia;
	}

	public TipoConta getTipoContaOrigemDinheiro() {
		return tipoContaOrigemDinheiro;
	}

}
