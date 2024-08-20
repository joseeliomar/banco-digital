package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;

@FeignClient(name = "conta-digital-cliente-ms")
public interface ContaDigitalClienteMsFeignClient {
	
	@GetMapping("/contaDigitalPessoaFisica/{cpf}")
	public ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> buscaContaDigitalPessoaFisica(@PathVariable String cpf);
	
	@GetMapping("/contaDigitalPessoaFisica/{agencia}/{conta}")
	public ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> buscaContaDigitalPessoaFisica(@PathVariable String agencia,
			@PathVariable String conta);
	
	@GetMapping("/contaDigitalPessoaJuridica/{cnpj}")
	public ResponseEntity<ContaDigitalPessoaJuridicaDTO1Busca> buscaContaDigitalPessoaJuridica(@PathVariable String cnpj);
}
