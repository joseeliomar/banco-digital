package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaSaqueContaPessoaFisicaDto;
import com.example.service.SaqueContaCorrentePessoaFisicaService;

@RestController
@RequestMapping("/saqueContaCorrentePessoaFisica")
public class SaqueContaCorrentePessoaFisicaController {

	@Autowired
	private SaqueContaCorrentePessoaFisicaService saqueContaCorrentePessoaFisicaService;

	@PostMapping("/")
	public ResponseEntity<?> efetuaSaqueContaPessoaFisica(
			@RequestBody DadosParaSaqueContaPessoaFisicaDto dadosParaSaqueContaPessoaFisicaDto) {
		saqueContaCorrentePessoaFisicaService
				.efetuaSaqueContaCorrentePessoaFisica(dadosParaSaqueContaPessoaFisicaDto);
		return ResponseEntity.ok().build();
	}
}
