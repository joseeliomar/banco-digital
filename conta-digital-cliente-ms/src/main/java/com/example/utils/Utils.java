package com.example.utils;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

	/**
	 * Obtém a URI do recurso inserido.
	 * 
	 * @param idRecursoInserido
	 * @return a URI do recurso inserido.
	 */
	public static URI obtemUriRecursoCriado(Object idRecursoInserido) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idRecursoInserido).toUri();
		return uri;
	}
	
	/**
	 * Retorna true se a string estiver nula ou vazia ou só com espaços em branco.
	 * 
	 * @param string
	 * @return true se a string estiver nula ou vazia ou só com espaços em branco.
	 */
	public static boolean stringNulaVaziaOuEmBranco(String string) {
		return (string == null || string.isBlank());
	}
}
