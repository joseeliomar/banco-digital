package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.openfeign.feignclient.dto.DadosContaDto;

@FeignClient(name = "gerador-numero-conta-afins-ms")
public interface GeradorNumeroContaAfinsMsFeignClient {
	
	@GetMapping("/geradorNumeroContaAfins/")
	public ResponseEntity<DadosContaDto> geraDadosConta();
}