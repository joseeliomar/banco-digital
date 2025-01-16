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
		if (valor == null) return null;
		
		for (StatusOkProcessamentoOperacao item : values()) {
			if (valor.contains(item.valor)) { // usando contains por conta de que a string de confirmação do status pode
												// estar acompanhada de por exemplo um ponto final.
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
