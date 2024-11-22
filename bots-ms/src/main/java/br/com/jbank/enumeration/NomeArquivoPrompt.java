package br.com.jbank.enumeration;

public enum NomeArquivoPrompt {
	PROMPT_INICIAL("PROMPT_INICIAL"),
	PROMPT_PARA_EXTRACAO_DADOS_NECESSARIOS_TRANSFERENCIA_BANCARIA("PROMPT_PARA_EXTRACAO_DADOS_NECESSARIOS_TRANSFERENCIA_BANCARIA"),
	PROMPT_PARA_EXTRACAO_DADOS_NECESSARIO_CONSULTA_SALDO("PROMPT_PARA_EXTRACAO_DADOS_NECESSARIO_CONSULTA_SALDO");

	private String nome;

	private NomeArquivoPrompt(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}
