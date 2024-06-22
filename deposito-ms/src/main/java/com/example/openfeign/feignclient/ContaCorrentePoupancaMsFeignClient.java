package com.example.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaAlteradaDto;
import com.example.enumeration.TipoConta;

@Component
@FeignClient(name = "conta-corrente-poupanca-ms", path = "/contaPessoaFisica")
public interface ContaPessoaFisicaFeignClient {
	
	@GetMapping("/{cpf}/{tipoConta}")
	public ResponseEntity<?> buscaContaPessoaFisica(@PathVariable String cpf, @PathVariable TipoConta tipoConta);

	@PutMapping("/")
	public ResponseEntity<ContaPessoaFisicaAlteradaDto> alteraContaPessoaFisica(
			@RequestBody ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto);
}
