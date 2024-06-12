package com.example.dto;

import java.io.Serializable;
import java.util.Objects;

import com.example.enumeration.UnidadeFederativa;

public class EnderecoAlteracaoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String rua;
	private Integer numero;
	private String bairro;
	private String municipio;
	private UnidadeFederativa unidadeFederativa;
	private String cep;

	public EnderecoAlteracaoDto(Long id, String rua, Integer numero, String bairro, String municipio,
			UnidadeFederativa unidadeFederativa, String cep) {
		this.id = id;
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.municipio = municipio;
		this.unidadeFederativa = unidadeFederativa;
		this.cep = cep;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoAlteracaoDto other = (EnderecoAlteracaoDto) obj;
		return Objects.equals(id, other.id);
	}

}
