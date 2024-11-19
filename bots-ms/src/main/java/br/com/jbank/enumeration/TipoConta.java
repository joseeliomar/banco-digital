package br.com.jbank.enumeration;

public enum TipoConta {
	CORRENTE("Corrente"),
	POUPANCA("Poupança");
	
	private String nome;

	private TipoConta(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}
