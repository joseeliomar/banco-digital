package com.example.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

	/**
	 * Obtém a URI do recurso inserido.
	 * 
	 * @param idRecursoInserido
	 * @return a URI do recurso inserido.
	 */
	public static URI obtemUriRecursoCriado(LinkedHashMap<String, Object> variaveisUri) {
		String path = "";
		List<Object> valoresVriaveisUri = new ArrayList<>();
		
		for (Entry<String, Object> variavel : variaveisUri.entrySet()) {
			path += "/" + "{" + variavel.getKey() + "}";
			valoresVriaveisUri.add(variavel.getValue());
		}
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(path).buildAndExpand(valoresVriaveisUri.toArray()).toUri();
		return uri;
	}
}
