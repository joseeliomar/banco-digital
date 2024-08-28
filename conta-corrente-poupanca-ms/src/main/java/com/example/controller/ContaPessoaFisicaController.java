package com.example.controller;

import java.net.URI;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaAlteradaDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaInseridaDto;
import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaFisica;
import com.example.service.ContaPessoaFisicaService;
import com.example.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contaPessoaFisica")
@Tag(name = "Conta corrente e conta poupança para pessoa física")
public class ContaPessoaFisicaController {

	@Autowired
	private ContaPessoaFisicaService contaPessoaFisicaService;

	@Operation(summary = "Realiza a inserção de uma conta corrente ou conta poupança")
	@PostMapping("/")
	public ResponseEntity<ContaPessoaFisicaInseridaDto> insereContaPessoaFisica(
			@RequestBody ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto) {
		ContaPessoaFisica contaPessoaFisica = contaPessoaFisicaService
				.insereContaPessoaFisica(contaPessoaFisicaInsercaoDto);
		ContaPessoaFisicaInseridaDto contaPessoaFisicaInseridaDto = new ContaPessoaFisicaInseridaDto(
				contaPessoaFisica);
		
		LinkedHashMap<String, Object> variaveisUri = new LinkedHashMap<String, Object>();
		variaveisUri.put("cpf", contaPessoaFisicaInseridaDto.getCpf());
		variaveisUri.put("tipoConta", contaPessoaFisicaInseridaDto.getTipoConta().name());
		
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(variaveisUri);
		return ResponseEntity.created(uriRecursoCriado).body(contaPessoaFisicaInseridaDto);
	}
	
	@Operation(summary = "Realiza a busca de uma conta corrente ou conta poupança")
	@GetMapping("/{cpf}/{tipoConta}")
	public ResponseEntity<?> buscaContaPessoaFisica(@PathVariable String cpf, @PathVariable TipoConta tipoConta) {
		ContaPessoaFisicaBuscaDto1 contaPessoaFisicaBuscaDto1 = contaPessoaFisicaService
				.buscaContaPessoaFisicaCompleta(cpf, tipoConta);
		
		if (contaPessoaFisicaBuscaDto1 != null) {
			return ResponseEntity.ok(contaPessoaFisicaBuscaDto1);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Realiza a alteração de uma conta corrente ou conta poupança")
	@PutMapping("/")
	public ResponseEntity<ContaPessoaFisicaAlteradaDto> alteraContaPessoaFisica(
			@RequestBody ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto) {
		ContaPessoaFisica contaPessoaFisica = contaPessoaFisicaService
				.alteraContaPessoaFisica(contaPessoaFisicaAlteracaoDto);
		return ResponseEntity.ok(new ContaPessoaFisicaAlteradaDto(contaPessoaFisica));
	}
	
	@Operation(summary = "Realiza a exclusão de uma conta corrente ou conta poupança")
	@DeleteMapping("/{idContaPessoaFisica}")
	public ResponseEntity<?> removeContaPessoaFisica(@PathVariable Long idContaPessoaFisica) {
		contaPessoaFisicaService.removeContaPessoaFisica(idContaPessoaFisica);
		return ResponseEntity.noContent().build();
	}
}
