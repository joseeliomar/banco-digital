package br.com.jbank.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.jbank.dto.ContaDigitalPessoaFisicaDTO2Busca;

@FeignClient(name = "conta-digital-cliente-ms")
public interface ContaDigitalClienteMsFeignClient {
	
	@GetMapping("/contaDigitalPessoaFisica/2/{cpf}")
	public ResponseEntity<ContaDigitalPessoaFisicaDTO2Busca> buscaContaDigitalPeloCpfComRespostaComSenha(@PathVariable String cpf);
	
}
