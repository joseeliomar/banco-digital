package br.com.jbank.utils;

public class Utils {

	/**
	 * Formata valor monetário em reais.
	 * 
	 * @param valor
	 * @return valor formatado.
	 */
	public static String formataValorReais(double valor) {
		return "R$ " + String.valueOf(valor).replace(".", ",");
	}
}
