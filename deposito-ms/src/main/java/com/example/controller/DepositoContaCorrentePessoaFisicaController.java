package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaDepositoContaPessoaFisicaDto;
import com.example.service.DepositoContaCorrentePessoaFisicaService;

@RestController
@RequestMapping("/depositoContaCorrentePessoaFisica")
public class DepositoContaCorrentePessoaFisicaController {

	@Autowired
	private DepositoContaCorrentePessoaFisicaService depositoService;

	@PostMapping("/")
	public ResponseEntity<?> efetuaDepositoContaPessoaFisica(
			@RequestBody DadosParaDepositoContaPessoaFisicaDto dadosParaDepositoContaPessoaFisicaDto) {
		depositoService.efetuaDepositoContaCorrentePessoaFisica(dadosParaDepositoContaPessoaFisicaDto);
		return ResponseEntity.ok().build();
	}
}
