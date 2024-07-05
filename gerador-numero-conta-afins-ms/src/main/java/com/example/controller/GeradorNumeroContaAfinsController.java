package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosContaDto;
import com.example.service.ContaService;

@RestController
@RequestMapping("/geradorNumeroContaAfins")
public class GeradorNumeroContaAfinsController {

	@Autowired
	private ContaService contaService;

	@GetMapping("/")
	public ResponseEntity<DadosContaDto> geraDadosConta() {
		DadosContaDto dadosContaGerada = contaService.geraDadosConta();
		return ResponseEntity.ok(dadosContaGerada);
	}
}
