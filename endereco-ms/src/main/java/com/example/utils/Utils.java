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
}
