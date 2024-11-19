package br.com.jbank.openai.enumeration;

public enum Role {

	SYSTEM("system"), ASSISTANT("assistant"), USER("user");

	private String texto;

	private Role(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return this.texto;
	}
}
