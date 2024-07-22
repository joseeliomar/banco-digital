package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaSaqueContaPessoaJuridicaDto;
import com.example.service.SaqueContaCorrentePessoaJuridicaService;

@RestController
@RequestMapping("/saqueContaCorrentePessoaJuridica")
public class SaqueContaCorrentePessoaJuridicaController {

	@Autowired
	private SaqueContaCorrentePessoaJuridicaService saqueContaCorrentePessoaJuridicaService;

	@PostMapping("/")
	public ResponseEntity<?> efetuaSaqueContaPessoaJuridica(
			@RequestBody DadosParaSaqueContaPessoaJuridicaDto dadosParaSaqueContaPessoaJuridicaDto) {
		saqueContaCorrentePessoaJuridicaService
				.efetuaSaqueContaCorrentePessoaJuridica(dadosParaSaqueContaPessoaJuridicaDto);
		return ResponseEntity.ok().build();
	}
}
