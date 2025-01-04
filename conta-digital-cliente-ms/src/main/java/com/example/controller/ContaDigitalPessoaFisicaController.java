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
import com.example.dto.ContaDigitalPessoaFisicaDTO2Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.dto.ContaDigitalPessoaFisicaInseridaDto;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.ContaDigitalPessoaFisicaService;
import com.example.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contaDigitalPessoaFisica")
@Tag(name = "Conta digital para pessoa física")
public class ContaDigitalPessoaFisicaController {

	@Autowired
	private ContaDigitalPessoaFisicaService contaDigitalPessoaFisicaService;

	@Operation(summary = "Realiza a inserção de uma conta digital")
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

	@Operation(summary = "Realiza a busca de uma conta digital pelo CPF (a senha da conta não estará inclusa na resposta)")
	@GetMapping("/{cpf}")
	public ResponseEntity<?> buscaContaDigitalPessoaFisica(@PathVariable String cpf) {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPeloCpfComRespostaSemSenha(cpf);

		if (contaDigitalPessoaFisicaDTO1Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
		}

		return ResponseEntity.notFound().build();
	}

	@Operation(summary = "Realiza a busca de uma conta digital pelo CPF (a senha da conta estará inclusa na resposta)")
	@GetMapping("/2/{cpf}")
	public ResponseEntity<?> buscaContaDigitalPeloCpfComRespostaComSenha(@PathVariable String cpf) {
		ContaDigitalPessoaFisicaDTO2Busca contaDigitalPessoaFisicaDTO2Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPeloCpfComRespostaComSenha(cpf);

		if (contaDigitalPessoaFisicaDTO2Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaFisicaDTO2Busca);
		}

		return ResponseEntity.notFound().build();
	}

	@Operation(summary = "Realiza a busca de uma conta digital pela agência e conta")
	@GetMapping("/{agencia}/{conta}")
	public ResponseEntity<?> buscaContaDigitalPessoaFisica(@PathVariable String agencia, @PathVariable String conta) {
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisicaDTO1Busca = contaDigitalPessoaFisicaService
				.buscaContaDigitalPelaAgenciaContaComRespostaSemSenha(agencia, conta);

		if (contaDigitalPessoaFisicaDTO1Busca != null) {
			return ResponseEntity.ok(contaDigitalPessoaFisicaDTO1Busca);
		}

		return ResponseEntity.notFound().build();
	}

	@Operation(summary = "Realiza a alteração de uma conta digital")
	@PutMapping("/")
	public ResponseEntity<ContaDigitalPessoaFisicaAlteradaDto> alteraContaDigitalPessoaFisica(
			@RequestBody ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto) {
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = contaDigitalPessoaFisicaService
				.alteraContaDigitalPessoaFisica(contaDigitalPessoaFisicaAlteracaoDto);
		return ResponseEntity.ok(new ContaDigitalPessoaFisicaAlteradaDto(contaDigitalPessoaFisica));
	}

	@Operation(summary = "Realiza a exclusão de uma conta digital")
	@DeleteMapping("/{cpf}")
	public ResponseEntity<?> removeContaDigitalPessoaFisica(@PathVariable String cpf) {
		contaDigitalPessoaFisicaService.removeContaDigitalPessoaFisica(cpf);
		return ResponseEntity.noContent().build();
	}
}
