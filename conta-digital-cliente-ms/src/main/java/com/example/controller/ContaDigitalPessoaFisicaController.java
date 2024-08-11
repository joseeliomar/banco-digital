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

import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaAlteradaDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.dto.ContaDigitalPessoaFisicaInseridaDto;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.ContaDigitalPessoaFisicaService;
import com.example.utils.Utils;

@RestController
@RequestMapping("/contaDigitalPessoaFisica")
public class ContaDigitalPessoaFisicaController {

	@Autowired
	private ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService;

	@PostMapping("/")
	public ResponseEntity<ContaDigitalPessoaFisicaInseridaDto> insereContaDigitalPessoaFisica(
			@RequestBody ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto) {
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaService
				.insereContaDigitalPessoaFisica(contaDigitalPessoaFisicaInsercaoDto);
		ContaDigitalPessoaFisicaInseridaDto contaDigitalPessoaFisicaInseridaDto = new ContaDigitalPessoaFisicaInseridaDto(
				contaDigitalPessoaFisica);
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(contaDigitalPessoaFisicaInseridaDto.getCpf());
		return ResponseEntity.created(uriRecursoCriado).body(contaDigitalPessoaFisicaInseridaDto);
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<?> buscaContaDigitalPessoaFisica(@PathVariable String cpf) {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPeloCpfComRespostaSemSenha(cpf);
		
		if (contaDigitalPessoaFisicaDTO1Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{agencia}/{conta}")
	public ResponseEntity<?> buscaContaDigitalPessoaFisica(@PathVariable String agencia, @PathVariable String conta) {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPelaAgenciaContaComRespostaSemSenha(agencia, conta);
		
		if (contaDigitalPessoaFisicaDTO1Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/")
	public ResponseEntity<ContaDigitalPessoaFisicaAlteradaDto> alteraContaDigitalPessoaFisica(
			@RequestBody ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto) {
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaService
				.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisicaAlteracaoDto);
		return ResponseEntity.ok(new ContaDigitalPessoaFisicaAlteradaDto(contaDigitalPessoaFisica));
	}
	
	@DeleteMapping("/{cpf}")
	public ResponseEntity<?> removeContaDigitalPessoaFisica(@PathVariable String cpf) {
		contaDigitalPessoaFisicaService.removeContaDigitalPessoaFisica(cpf);
		return ResponseEntity.noContent().build();
	}
}
