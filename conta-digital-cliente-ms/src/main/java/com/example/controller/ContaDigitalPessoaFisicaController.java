package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.ContaDigitalPessoaFisicaService;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.services.controller.dto.ContaDigitalPessoaFisicaInsercaoDto;

@RestController
@RequestMapping("/contaDigitalPessoaFisica")
public class ContaDigitalPessoaFisicaController {

	@Autowired
	private ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService;

	@PostMapping("/")
	public ResponseEntity<ContaDigitalPessoaFisica> insereContaDigitalPessoaFisica(
			@RequestBody ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto) {
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaService
				.insereContaDigitalPessoaFisica(contaDigitalPessoaFisicaInsercaoDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(contaDigitalPessoaFisica);
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<?> buscaContaDigitalPessoaFisica(@PathVariable String cpf) {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPeloCpfComRespostaSemSenha(cpf);
		return ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
	}
	
	@PutMapping("/")
	public ResponseEntity<ContaDigitalPessoaFisica> alteraContaDigitalPessoaFisica(
			@RequestBody ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto) {
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaService
				.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisicaAlteracaoDto);
		return ResponseEntity.ok(contaDigitalPessoaFisica);
	}
	
	@DeleteMapping("/{cpf}")
	public ResponseEntity<?> removeContaDigitalPessoaFisica(@PathVariable String cpf) {
		contaDigitalPessoaFisicaService.removeContaDigitalPessoaFisica(cpf);
		return ResponseEntity.noContent().build();
	}
}
