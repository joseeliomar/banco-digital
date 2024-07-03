package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaInseridaDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaInseridaDto;
import com.example.enumeration.TipoConta;

@FeignClient(name = "conta-corrente-poupanca-ms")
public interface ContaCorrentePoupancaMsFeignClient {

	@PostMapping("/contaPessoaFisica/")
	public ResponseEntity<ContaPessoaFisicaInseridaDto> insereContaPessoaFisica(
			@RequestBody ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto);
	
	@PostMapping("/contaPessoaJuridica/")
	public ResponseEntity<ContaPessoaJuridicaInseridaDto> insereContaPessoaJuridica(
			@RequestBody ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto);

	@GetMapping("/contaPessoaFisica/{cpf}/{tipoConta}")
	public ResponseEntity<ContaPessoaFisicaBuscaDto1> buscaContaPessoaFisica(@PathVariable String cpf, @PathVariable TipoConta tipoConta);
	
	@GetMapping("/contaPessoaJuridica/{cnpj}/{tipoConta}")
	public ResponseEntity<ContaPessoaJuridicaBuscaDto1> buscaContaPessoaJuridica(@PathVariable String cnpj, @PathVariable TipoConta tipoConta);
	
	@DeleteMapping("/contaPessoaFisica/{idContaPessoaFisica}")
	public ResponseEntity<?> removeContaPessoaFisica(@PathVariable Long idContaPessoaFisica);
	
	@DeleteMapping("/contaPessoaJuridica/{idContaPessoaJuridica}")
	public ResponseEntity<?> removeContaPessoaJuridica(@PathVariable Long idContaPessoaJuridica);

}
