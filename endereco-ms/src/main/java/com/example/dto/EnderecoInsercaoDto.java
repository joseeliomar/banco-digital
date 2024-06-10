package com.example.dto;

import com.example.enumeration.UnidadeFederativa;

public record EnderecoInsercaoDto(String rua, int numero, String bairro, String municipio, UnidadeFederativa unidadeFederativa,
		String cep) {

}
