package br.com.jbank.enumeration;

public enum MensagemPadronizada {

	MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO(
			"Caso o problema persista, por favor, utilize o nosso aplicativo ou o nosso internet "
					+ "bank. Problemas acontecem, mas estamos sempre trabalhando para resolve-los e para "
					+ "melhorar os nossos produtos e serviços. Nos desculpe pelo transtorno.");

	private String texto;

	private MensagemPadronizada(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

}
