package com.example.utils;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

	/**
	 * Obtém a localização do recurso inserido.
	 * 
	 * @param idRecursoInserido
	 * @return a localização do recurso inserido.
	 */
	public static URI obtemLocalizacaoRecursoCriado(String idRecursoInserido) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idRecursoInserido).toUri();
		return uri;
	}
}
