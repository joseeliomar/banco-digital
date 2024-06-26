package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaInseridaDto;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaInseridaDto;

@FeignClient(name = "conta-corrente-poupanca-ms")
public interface ContaCorrentePoupancaMsFeignClient {

	@PostMapping("/")
	public ResponseEntity<ContaPessoaFisicaInseridaDto> insereContaPessoaFisica(
			@RequestBody ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto);
	
	@PostMapping("/")
	public ResponseEntity<ContaPessoaJuridicaInseridaDto> insereContaPessoaJuridica(
			@RequestBody ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto);

}
