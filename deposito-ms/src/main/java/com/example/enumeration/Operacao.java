package com.example.enumeration;

public enum Operacao {
	DEPOSITO("Depósito"),
	SAQUE("Saque"),
	TRANSFERENCIA_PARA_OUTRA_INSTITUICAO_FINANCEIRA("Transferência para outra instituição financeira"),
	TRANSFERENCIA_VINDA_DE_OUTRA_INSTITUICAO_FINANCEIRA("Transferência vinda de outra instituição financeira"),
	TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA_ENTRADA_DINHEIRO("Transferência para a mesma instituição financeira (entrada de dinheiro)"),
	TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA_SAIDA_DINHEIRO("Transferência para a mesma instituição financeira (saída de dinheiro)");
	
	private String descricao;

	private Operacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
