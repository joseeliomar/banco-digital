package com.example.enumeration;

public enum TipoConta {
	CORRENTE("CORRENTE"),
	POUPANCA("POUPANÇA");
	
	String nome;

	private TipoConta(String nome) {
		this.nome = nome;
	}
}
