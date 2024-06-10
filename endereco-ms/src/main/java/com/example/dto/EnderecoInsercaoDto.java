package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.UnidadeFederativa;

public class EnderecoInsercaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String rua;
	private Integer numero;
	private String bairro;
	private String municipio;
	private UnidadeFederativa unidadeFederativa;
	private String cep;

	public EnderecoInsercaoDto(String rua, Integer numero, String bairro, String municipio,
			UnidadeFederativa unidadeFederativa, String cep) {
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.municipio = municipio;
		this.unidadeFederativa = unidadeFederativa;
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bairro, cep, municipio, numero, rua, unidadeFederativa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoInsercaoDto other = (EnderecoInsercaoDto) obj;
		return Objects.equals(bairro, other.bairro) && Objects.equals(cep, other.cep)
				&& Objects.equals(municipio, other.municipio) && numero == other.numero
				&& Objects.equals(rua, other.rua) && unidadeFederativa == other.unidadeFederativa;
	}
}
