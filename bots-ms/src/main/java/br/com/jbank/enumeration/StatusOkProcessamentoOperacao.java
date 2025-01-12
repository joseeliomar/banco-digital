package br.com.jbank.enumeration;

public enum StatusOkProcessamentoOperacao {
	STATUS_PARA_PROCESSAMENTO_TRANSFERENCIA("OK_TUDO_PRONTO_PROCESSAMENTO_TRANSFERENCIA",
			"OK! Tudo pronto para o processamento da transferência"),
	STATUS_PARA_CONSULTA_SALDO("OK_TUDO_PRONTO_PARA_CONSULTA_SALDO",
			"OK! Tudo pronto para realização da consulta do saldo.");
	
	private String valor;
	private String descricao;

	private StatusOkProcessamentoOperacao(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public static StatusOkProcessamentoOperacao obterItem(String valor) {
		for (StatusOkProcessamentoOperacao item: values()) {
			if (item.valor.equals(valor)) {
				return item;
			}
		}
		return null;
	}

	public String getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}

}
