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
			@RequestBody DadosParaTransferenciaEntreContasMesmoClienteDto dadosParaTransferenciaEntreContasMesmoClienteDto) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferenciaEntreContasMesmoClienteDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/efetuaTransferenciaEntreContasClientesDiferentesDesseBanco")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			@RequestBody DadosParaTransferenciaEntreContasClientesDiferentesDto dadosParaTransferenciaEntreContasMesmoUsuarioDto) {
		transferenciaComContasPessoaFisicaService.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
				dadosParaTransferenciaEntreContasMesmoUsuarioDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			@RequestBody DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
						dadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/recebeDinheiroVindoOutraInstituicaoFinanceira")
	public ResponseEntity<?> recebeDinheiroVindoOutraInstituicaoFinanceira(
			@RequestBody DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto) {
		transferenciaComContasPessoaFisicaService.recebeDinheiroVindoOutraInstituicaoFinanceira(
				dadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceiraDto);
		return ResponseEntity.ok().build();
	}
}