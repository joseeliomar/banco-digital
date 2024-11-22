package br.com.jbank.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.jbank.dto.ContaPessoaFisicaBuscaDto1;
import br.com.jbank.enumeration.TipoConta;

@FeignClient(name = "conta-corrente-poupanca-ms")
public interface ContaCorrentePoupancaMsFeignClient {

	@GetMapping("/contaPessoaFisica/{cpf}/{tipoConta}")
	public ResponseEntity<ContaPessoaFisicaBuscaDto1> buscaContaPessoaFisica(@PathVariable String cpf,
			@PathVariable TipoConta tipoConta);

}
