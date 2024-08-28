package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.example.service.DepositoContaCorrentePessoaFisicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/depositoContaCorrentePessoaFisica")
@Tag(name = "Depósito em conta corrente de pessoa física")
public class DepositoContaCorrentePessoaFisicaController {

	@Autowired
	private DepositoContaCorrentePessoaFisicaService depositoContaCorrentePessoaFisicaService;

	@Operation(summary = "Realiza um depósito")
	@PostMapping("/")
	public ResponseEntity<?> efetuaDepositoContaPessoaFisica(
			@RequestBody DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoContaPessoaFisicaDto) {
		depositoContaCorrentePessoaFisicaService
				.efetuaDepositoContaCorrentePessoaFisica(dadosParaDepositoContaPessoaFisicaDto);
		return ResponseEntity.ok().build();
	}
}
