package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaSaqueContaPessoaFisicaDto;
import com.example.service.SaqueContaCorrentePessoaFisicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/saqueContaCorrentePessoaFisica")
@Tag(name = "Saque em conta corrente de pessoa física")
public class SaqueContaCorrentePessoaFisicaController {

	@Autowired
	private SaqueContaCorrentePessoaFisicaService saqueContaCorrentePessoaFisicaService;

	@Operation(summary = "Realiza um saque")
	@PostMapping("/")
	public ResponseEntity<?> efetuaSaqueContaPessoaFisica(
			@RequestBody DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueContaPessoaFisicaDto) {
		saqueContaCorrentePessoaFisicaService
				.efetuaSaqueContaCorrentePessoaFisica(dadosParaSaqueContaPessoaFisicaDto);
		return ResponseEntity.ok().build();
	}
}
