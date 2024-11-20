package br.com.jbank.dto;

import br.com.jbank.enumeration.Banco;
import br.com.jbank.enumeration.TipoConta;

public class DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto
		extends DadosParaTransferenciaBancaria {
	private static final long serialVersionUID = 1L;

	private String cpfClienteDonoContaQueDinheiroSai;
	private TipoConta tipoContaOrigemDinheiro;
	private Banco bancoDestino;
	private String agenciaDestino;
	private String contaDestino;

	public String getCpfClienteDonoContaQueDinheiroSai() {
		return cpfClienteDonoContaQueDinheiroSai;
	}

	public TipoConta getTipoContaOrigemDinheiro() {
		return tipoContaOrigemDinheiro;
	}

	public Banco getBancoDestino() {
		return bancoDestino;
	}

	public String getAgenciaDestino() {
		return agenciaDestino;
	}

	public String getContaDestino() {
		return contaDestino;
	}

}
