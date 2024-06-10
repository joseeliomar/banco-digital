package com.example.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.EnderecoInsercaoDto;
import com.example.enumeration.UnidadeFederativa;
import com.example.model.Endereco;
import com.example.repository.EnderecoRepository;

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Endereco insereEndereco(EnderecoInsercaoDto enderecoInsercaoDto1) {
		String rua = enderecoInsercaoDto1.rua();
		int numero = enderecoInsercaoDto1.numero();
		String bairro = enderecoInsercaoDto1.bairro();
		String municipio = enderecoInsercaoDto1.municipio();
		UnidadeFederativa unidadeFederativa = enderecoInsercaoDto1.unidadeFederativa();
		String cep = enderecoInsercaoDto1.cep();
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		Endereco endereco = new Endereco(rua, numero, bairro, municipio, unidadeFederativa, cep, dataHoraCadastro, null);
		return enderecoRepository.save(endereco);
	}

}
