package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaDepositoContaPessoaJuridicaDto;
import com.example.service.DepositoContaCorrentePessoaJuridicaService;

@RestController
@RequestMapping("/depositoContaCorrentePessoaJuridica")
public class DepositoContaCorrentePessoaJuridicaController {

	@Autowired
	private DepositoContaCorrentePessoaJuridicaService depositoService;

	@PostMapping("/")
	public ResponseEntity<?> efetuaDepositoContaPessoaJuridica(
			@RequestBody DadosParaDepositoContaPessoaJuridicaDto dadosParaDepositoContaPessoaJuridicaDto) {
		depositoService.efetuaDepositoContaCorrentePessoaJuridica(dadosParaDepositoContaPessoaJuridicaDto);
		return ResponseEntity.ok().build();
	}
}
