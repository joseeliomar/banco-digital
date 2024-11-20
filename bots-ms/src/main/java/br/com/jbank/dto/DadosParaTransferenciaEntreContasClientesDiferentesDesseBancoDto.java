package br.com.jbank.dto;

import br.com.jbank.enumeration.TipoConta;

public class DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto extends DadosParaTransferenciaBancaria {
	private static final long serialVersionUID = 1L;
	
	private String cpfClienteDonoContaQueDinheiroSai;
	private TipoConta tipoContaOrigemDinheiro;
	private String agenciaDestinoDinheiro;
	private String contaDestinoDinheiro;

	public String getCpfClienteDonoContaQueDinheiroSai() {
		return cpfClienteDonoContaQueDinheiroSai;
	}

	public TipoConta getTipoContaOrigemDinheiro() {
		return tipoContaOrigemDinheiro;
	}

	public String getAgenciaDestinoDinheiro() {
		return agenciaDestinoDinheiro;
	}

	public String getContaDestinoDinheiro() {
		return contaDestinoDinheiro;
	}

}
