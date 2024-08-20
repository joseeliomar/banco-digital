package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira;
import com.example.dto.DadosParaTransferenciaEntreContasClientesDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import com.example.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;
import com.example.service.TransferenciaComContasPessoaFisicaService;

@RestController
@RequestMapping("/transferenciaDinheiroPessoaFisica")
public class TransferenciaDinheiroPessoaFisicaController {

	@Autowired
	private TransferenciaComContasPessoaFisicaService transferenciaComContasPessoaFisicaService;

	@PostMapping("/efetuaTransferenciaEntreContasMesmoCliente")
	public ResponseEntity<?> efetuaTransferenciaEntreContasMesmoCliente(
			@RequestBody DadosParaTransferenciaEntreContasMesmoClienteDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService.efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/efetuaTransferenciaEntreContasClientesDiferentesDesseBanco")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			@RequestBody DadosParaTransferenciaEntreContasClientesDiferentesDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			@RequestBody DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/recebeDinheiroVindoOutraInstituicaoFinanceira")
	public ResponseEntity<?> recebeDinheiroVindoOutraInstituicaoFinanceira(
			@RequestBody DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira dadosParaRecebimentoDinheiro) {
		transferenciaComContasPessoaFisicaService
				.recebeDinheiroVindoOutraInstituicaoFinanceira(dadosParaRecebimentoDinheiro);
		return ResponseEntity.ok().build();
	}
}