package com.example.controller;

import java.net.URI;

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

import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaAlteradaDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaInseridaDto;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.service.ContaDigitalPessoaJuridicaService;
import com.example.utils.Utils;

@RestController
@RequestMapping("/contaDigitalPessoaJuridica")
public class ContaDigitalPessoaJuridicaController {

	@Autowired
	private ContaDigitalPessoaJuridicaService contaDigitalPessoaJuridicaService;

	@PostMapping("/")
	public ResponseEntity<ContaDigitalPessoaJuridicaInseridaDto> insereContaDigitalPessoaJuridica(
			@RequestBody ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto) {
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridica = contaDigitalPessoaJuridicaService
				.insereContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaInsercaoDto);
		ContaDigitalPessoaJuridicaInseridaDto contaDigitalPessoaJuridicaInseridaDto = new ContaDigitalPessoaJuridicaInseridaDto(
				contaDigitalPessoaJuridica);
		URI localizacaoRecursoCriado = Utils
				.obtemLocalizacaoRecursoCriado(contaDigitalPessoaJuridicaInseridaDto.getCnpj());
		return ResponseEntity.created(localizacaoRecursoCriado).body(contaDigitalPessoaJuridicaInseridaDto);
	}
	
	@GetMapping("/{cnpj}")
	public ResponseEntity<?> buscaContaDigitalPessoaJuridica(@PathVariable String cnpj) {
		ContaDigitalPessoaJuridicaDTO1Busca contaDigitalPessoaJuridicaDTO1Busca = contaDigitalPessoaJuridicaService
				.buscaContaDigitalPeloCnpjComRespostaSemSenha(cnpj);
		
		if (contaDigitalPessoaJuridicaDTO1Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaJuridicaDTO1Busca);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/")
	public ResponseEntity<ContaDigitalPessoaJuridicaAlteradaDto> alteraContaDigitalPessoaJuridica(
			@RequestBody ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaAlteracaoDto) {
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridica = contaDigitalPessoaJuridicaService
				.alteraContaDigitalPessoaJuridica(contaDigitalPessoaJuridicaAlteracaoDto);
		return ResponseEntity.ok(new ContaDigitalPessoaJuridicaAlteradaDto(contaDigitalPessoaJuridica));
	}
	
	@DeleteMapping("/{cnpj}")
	public ResponseEntity<?> removeContaDigitalPessoaJuridica(@PathVariable String cnpj) {
		contaDigitalPessoaJuridicaService.removeContaDigitalPessoaJuridica(cnpj);
		return ResponseEntity.noContent().build();
	}
}
