package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosGeracaoExtratoContaCorrentePessoaFisicaDto;
import com.example.dto.ExtratoContaCorrenteDto;
import com.example.service.ExtratoContaPessoaFisicaService;

@RestController
@RequestMapping("/extratoContaPessoaFisica")
public class ExtratoContaPessoaFisicaController {

	@Autowired
	private ExtratoContaPessoaFisicaService extratoContaPessoaFisicaService;
	
	@GetMapping("/corrente")
	public ResponseEntity<?> geraExtratoContaCorrente(@RequestBody DadosGeracaoExtratoContaCorrentePessoaFisicaDto dadosGeracaoExtrato) {
		ExtratoContaCorrenteDto extratoContaCorrenteDto = extratoContaPessoaFisicaService
				.geraExtratoContaCorrente(dadosGeracaoExtrato.cpfCliente(), dadosGeracaoExtrato.quantidadeDias());
		return ResponseEntity.ok(extratoContaCorrenteDto);
	}
}
