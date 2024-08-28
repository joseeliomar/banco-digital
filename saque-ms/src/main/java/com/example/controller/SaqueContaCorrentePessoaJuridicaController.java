package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaSaqueContaPessoaJuridicaDto;
import com.example.service.SaqueContaCorrentePessoaJuridicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/saqueContaCorrentePessoaJuridica")
@Tag(name = "Saque em conta corrente de pessoa jurídica")
public class SaqueContaCorrentePessoaJuridicaController {

	@Autowired
	private SaqueContaCorrentePessoaJuridicaService saqueContaCorrentePessoaJuridicaService;

	@Operation(summary = "Realiza um saque")
	@PostMapping("/")
	public ResponseEntity<?> efetuaSaqueContaPessoaJuridica(
			@RequestBody DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueContaPessoaJuridicaDto) {
		saqueContaCorrentePessoaJuridicaService
				.efetuaSaqueContaCorrentePessoaJuridica(dadosParaSaqueContaPessoaJuridicaDto);
		return ResponseEntity.ok().build();
	}
}
