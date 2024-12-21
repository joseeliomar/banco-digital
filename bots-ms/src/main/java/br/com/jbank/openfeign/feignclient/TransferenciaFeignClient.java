package br.com.jbank.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;

@FeignClient(name = "transferencia-ms", path = "/transferenciaDinheiroPessoaFisica")
public interface TransferenciaFeignClient {

	@PostMapping("/efetuaTransferenciaEntreContasMesmoCliente")
	public ResponseEntity<?> efetuaTransferenciaEntreContasMesmoCliente(
			@RequestBody DadosParaTransferenciaEntreContasMesmoClienteDto dadosParaTransferencia);

	@PostMapping("/efetuaTransferenciaEntreContasClientesDiferentesDesseBanco")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			@RequestBody DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto dadosParaTransferencia);

	@PostMapping("/efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			@RequestBody DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto dadosParaTransferencia);
}