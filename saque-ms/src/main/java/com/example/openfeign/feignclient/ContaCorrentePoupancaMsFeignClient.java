package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaAlteradaDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaAlteradaDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.enumeration.TipoConta;

@FeignClient(name = "conta-corrente-poupanca-ms")
public interface ContaCorrentePoupancaMsFeignClient {
	
	@GetMapping("/contaPessoaFisica/{cpf}/{tipoConta}")
	public ResponseEntity<ContaPessoaFisicaBuscaDto1> buscaContaPessoaFisica(@PathVariable String cpf, @PathVariable TipoConta tipoConta);

	@PutMapping("/contaPessoaFisica/")
	public ResponseEntity<ContaPessoaFisicaAlteradaDto> alteraContaPessoaFisica(
			@RequestBody ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto);
	
	@GetMapping("/contaPessoaJuridica/{cpf}/{tipoConta}")
	public ResponseEntity<ContaPessoaJuridicaBuscaDto1> buscaContaPessoaJuridica(@PathVariable String cpf, @PathVariable TipoConta tipoConta);

	@PutMapping("/contaPessoaJuridica/")
	public ResponseEntity<ContaPessoaJuridicaAlteradaDto> alteraContaPessoaJuridica(
			@RequestBody ContaPessoaJuridicaAlteracaoDto contaPessoaJuridicaAlteracaoDto);
}
