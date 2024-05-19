package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.model.ContaDigitalPessoaJuridica;
import com.example.repository.ContaDigitalPessoaJuridicaRepository;
import com.example.service.exceptions.ValidacaoException;

@Service
public class ContaDigitalPessoaJuridicaService {
	
	@Autowired
	private ContaDigitalPessoaJuridicaRepository repository;

	public ContaDigitalPessoaJuridica criaContaDigitalPessoaJuridica(ContaDigitalPessoaJuridica contaDigitalPessoaJuridica) {
		String razaoSocial = contaDigitalPessoaJuridica.getRazaoSocial();
		
		validaRazaoSocial(razaoSocial);
		
		return repository.save(contaDigitalPessoaJuridica);
	}

	private void validaRazaoSocial(String razaoSocial) {
		if (razaoSocial == null || razaoSocial.isBlank()) {
			throw new ValidacaoException("Razão social não informada.", HttpStatus.BAD_REQUEST);
		}
	}
}
