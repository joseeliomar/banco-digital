package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosContaDto;
import com.example.service.ContaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/geradorNumeroContaAfins")
@Tag(name = "Gerador de número de conta e afins")
public class GeradorNumeroContaAfinsController {

	@Autowired
	private ContaService contaService;

	@Operation(summary = "Realiza a geração de dados da conta")
	@GetMapping("/")
	public ResponseEntity<DadosContaDto> geraDadosConta() {
		DadosContaDto dadosContaGerada = contaService.geraDadosConta();
		return ResponseEntity.ok(dadosContaGerada);
	}
}
