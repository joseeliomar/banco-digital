package br.com.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.jbank.dto.ContaDigitalPessoaFisicaDTO2Busca;
import br.com.jbank.openfeign.feignclient.ContaDigitalClienteMsFeignClient;

@Service
public class DigitalAccountService {

	@Autowired
	private ContaDigitalClienteMsFeignClient contaDigitalClienteMsFeignClient;
	
	public ContaDigitalPessoaFisicaDTO2Busca findDigitalAccountPasswordByCpf(String cpf) {
		ResponseEntity<ContaDigitalPessoaFisicaDTO2Busca> response = contaDigitalClienteMsFeignClient.buscaContaDigitalPeloCpfComRespostaComSenha(cpf);
		ContaDigitalPessoaFisicaDTO2Busca digitalAccount = response.getBody();
		return digitalAccount;
	}
}
