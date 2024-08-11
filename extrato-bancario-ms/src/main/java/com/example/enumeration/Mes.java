package com.example.enumeration;

public enum Mes {
	JANEIRO(1, "Janeiro"),
	FEVEREIRO(2, "Fevereiro"),
	MARCO(3, "Março"),
	ABRIL(4, "Abril"),
	MAIO(5, "Maio"),
	JUNHO(6, "Junho"),
	JULHO(7, "Julho"),
	AGOSTO(8, "Agosto"),
	SETEMBRO(9, "Setembro"),
	OUTUBRO(10, "Outubro"),
	NOVEMBRO(11, "Novembro"),
	DEZEMBRO(12, "Dezembro");
	
	private int numero;
	private String nome;
	
	private Mes(int numero, String nome) {
		this.numero = numero;
		this.nome = nome;
	}
	
	public static Mes getMes(int numeroMes) {
		for (Mes mes : Mes.values()) {
			if (mes.numero == numeroMes) {
				return mes;
			}
		}
		return null;
	}

	public int getNumero() {
		return numero;
	}

	public String getNome() {
		return nome;
	}

}
