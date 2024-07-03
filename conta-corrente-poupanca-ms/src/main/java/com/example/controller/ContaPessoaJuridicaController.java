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

import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaAlteradaDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaInseridaDto;
import com.example.enumeration.TipoConta;
import com.example.model.ContaPessoaJuridica;
import com.example.service.ContaPessoaJuridicaService;
import com.example.utils.Utils;

@RestController
@RequestMapping("/contaPessoaJuridica")
public class ContaPessoaJuridicaController {

	@Autowired
	private ContaPessoaJuridicaService contaPessoaJuridicaService;

	@PostMapping("/")
	public ResponseEntity<ContaPessoaJuridicaInseridaDto> insereContaPessoaJuridica(
			@RequestBody ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto) {
		ContaPessoaJuridica contaPessoaJuridica = contaPessoaJuridicaService
				.insereContaPessoaJuridica(contaPessoaJuridicaInsercaoDto);
		ContaPessoaJuridicaInseridaDto contaPessoaJuridicaInseridaDto = new ContaPessoaJuridicaInseridaDto(
				contaPessoaJuridica);
		
		LinkedHashMap<String, Object> variaveisUri = new LinkedHashMap<String, Object>();
		variaveisUri.put("cnpj", contaPessoaJuridicaInseridaDto.getCnpj());
		variaveisUri.put("tipoConta", contaPessoaJuridicaInseridaDto.getTipoConta().name());
		
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(variaveisUri);
		return ResponseEntity.created(uriRecursoCriado).body(contaPessoaJuridicaInseridaDto);
	}
	
	@GetMapping("/{cnpj}/{tipoConta}")
	public ResponseEntity<?> buscaContaPessoaJuridica(@PathVariable String cnpj, @PathVariable TipoConta tipoConta) {
		ContaPessoaJuridicaBuscaDto1 contaPessoaJuridicaBuscaDto1 = contaPessoaJuridicaService
				.buscaContaPessoaJuridicaCompleta(cnpj, tipoConta);
		
		if (contaPessoaJuridicaBuscaDto1 != null) {
			return ResponseEntity.ok(contaPessoaJuridicaBuscaDto1);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/")
	public ResponseEntity<ContaPessoaJuridicaAlteradaDto> alteraContaPessoaJuridica(
			@RequestBody ContaPessoaJuridicaAlteracaoDto contaPessoaJuridicaAlteracaoDto) {
		ContaPessoaJuridica contaPessoaJuridica = contaPessoaJuridicaService
				.alteraContaPessoaJuridica(contaPessoaJuridicaAlteracaoDto);
		return ResponseEntity.ok(new ContaPessoaJuridicaAlteradaDto(contaPessoaJuridica));
	}
	
	@DeleteMapping("/{idContaPessoaJuridica}")
	public ResponseEntity<?> removeContaPessoaJuridica(@PathVariable Long idContaPessoaJuridica) {
		contaPessoaJuridicaService.removeContaPessoaJuridica(idContaPessoaJuridica);
		return ResponseEntity.noContent().build();
	}
}
