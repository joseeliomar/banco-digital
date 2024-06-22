package com.example.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInseridoDto;

@Component
@FeignClient(name = "extrato-bancario-ms", path = "/itemExtratoContaPessoaFisica")
public interface ItemExtratoContaPessoaFisicaFeignClient {

	@PostMapping("/")
	public ResponseEntity<ItemExtratoContaPessoaFisicaInseridoDto> insereItemExtratoContaPessoaFisica(
			@RequestBody ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto);

}
