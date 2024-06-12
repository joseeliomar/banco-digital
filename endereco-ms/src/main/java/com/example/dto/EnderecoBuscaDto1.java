package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.enumeration.UnidadeFederativa;
import com.example.model.Endereco;

public class EnderecoBuscaDto1 implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String rua;
	private Integer numero;
	private String bairro;
	private String municipio;
	private UnidadeFederativa unidadeFederativa;
	private String cep;
	private LocalDateTime dataHoraCadastro;
	private LocalDateTime dataHoraAlteracao;
	
	public EnderecoBuscaDto1() {
	}

	public EnderecoBuscaDto1(Endereco endereco) {
		id = endereco.getId();
		rua = endereco.getRua();
		numero = endereco.getNumero();
		bairro = endereco.getBairro();
		municipio = endereco.getMunicipio();
		unidadeFederativa = endereco.getUnidadeFederativa();
		cep = endereco.getCep();
		dataHoraCadastro = endereco.getDataHoraCadastro();
		dataHoraAlteracao = endereco.getDataHoraAlteracao();
	}

	public Long getId() {
		return id;
	}

	public String getRua() {
		return rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public String getBairro() {
		return bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public String getCep() {
		return cep;
	}

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
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
		EnderecoBuscaDto1 other = (EnderecoBuscaDto1) obj;
		return Objects.equals(id, other.id);
	}

}
