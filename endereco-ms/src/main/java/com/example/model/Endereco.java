package com.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.example.enumeration.UnidadeFederativa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Endereco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String rua;

	@Column(nullable = false, length = 4)
	private int numero;

	@Column(nullable = false, length = 40)
	private String bairro;

	@Column(nullable = false, length = 40)
	private String municipio;

	@Column(name = "unidade_federativa", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)
	private UnidadeFederativa unidadeFederativa;

	@Column(nullable = false, length = 8)
	private String cep;

	@Column(name = "data_hora_cadastro", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraCadastro;

	@Column(name = "data_hora_alteracao", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraAlteracao;
	
	public Endereco() {
	}
	
	public Endereco(String rua, int numero, String bairro, String municipio, UnidadeFederativa unidadeFederativa,
			String cep, LocalDateTime dataHoraCadastro, LocalDateTime dataHoraAlteracao) {
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.municipio = municipio;
		this.unidadeFederativa = unidadeFederativa;
		this.cep = cep;
		this.dataHoraCadastro = dataHoraCadastro;
		this.dataHoraAlteracao = dataHoraAlteracao;
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

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
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

	public LocalDateTime getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public void setDataHoraCadastro(LocalDateTime dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
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
		Endereco other = (Endereco) obj;
		return Objects.equals(id, other.id);
	}
}
