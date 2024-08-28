package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaDepositoContaPessoaJuridicaDto;
import com.example.service.DepositoContaCorrentePessoaJuridicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/depositoContaCorrentePessoaJuridica")
@Tag(name = "Depósito em conta corrente de pessoa jurídica")
public class DepositoContaCorrentePessoaJuridicaController {

	@Autowired
	private DepositoContaCorrentePessoaJuridicaService depositoContaCorrentePessoaJuridicaService;

	@Operation(summary = "Realiza um depósito")
	@PostMapping("/")
	public ResponseEntity<?> efetuaDepositoContaPessoaJuridica(
			@RequestBody DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoContaPessoaJuridicaDto) {
		depositoContaCorrentePessoaJuridicaService
				.efetuaDepositoContaCorrentePessoaJuridica(dadosParaDepositoContaPessoaJuridicaDto);
		return ResponseEntity.ok().build();
	}
}
