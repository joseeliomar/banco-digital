package com.example.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInseridoDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInseridoDto;

@FeignClient(name = "extrato-bancario-ms")
public interface ExtratoBancarioMsFeignClient {

	@PostMapping("/itemExtratoContaPessoaFisica/")
	public ResponseEntity<ItemExtratoContaPessoaFisicaInseridoDto> insereItemExtratoContaPessoaFisica(
			@RequestBody ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto);
	
	@PostMapping("/itemExtratoContaPessoaJuridica/")
	public ResponseEntity<ItemExtratoContaPessoaJuridicaInseridoDto> insereItemExtratoContaPessoaJuridica(
			@RequestBody ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto);

}
