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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/transferenciaDinheiroPessoaFisica")
@Tag(name = "Transferência de dinheiro para pessoa física")
public class TransferenciaDinheiroPessoaFisicaController {

	@Autowired
	private TransferenciaComContasPessoaFisicaService transferenciaComContasPessoaFisicaService;

	@Operation(summary = "Efetua transferência entre contas do mesmo cliente, ou seja, de conta corrente para "
			+ "conta poupança ou de conta poupança para conta corrente, sendo essa contas do mesmo cliente.")
	@PostMapping("/efetuaTransferenciaEntreContasMesmoCliente")
	public ResponseEntity<?> efetuaTransferenciaEntreContasMesmoCliente(
			@RequestBody DadosParaTransferenciaEntreContasMesmoClienteDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService.efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Efetua transferência entre contas de clientes diferentes desse banco (JBank), "
			+ "ou seja, da conta corrente de um determinado cliente para a conta corrente de outro "
			+ "determinado cliente ou da conta poupança de um determinado cliente para a conta corrente "
			+ "de outro determinado cliente (a conta de destino do dinheiro sempre será uma conta "
			+ "corrente).")
	@PostMapping("/efetuaTransferenciaEntreContasClientesDiferentesDesseBanco")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			@RequestBody DadosParaTransferenciaEntreContasClientesDiferentesDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Efetua transferência entre contas de instituições financeiras diferentes, "
			+ "ou seja, da conta corrente de um determinado cliente dessa instituição financeira "
			+ "para a conta de determinado cliente de outra instituição financeira ou da conta "
			+ "poupança de um determinado cliente dessa instituição financeira para a conta "
			+ "de determinado cliente de outra instituição financeira.")
	@PostMapping("/efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes")
	public ResponseEntity<?> efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			@RequestBody DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto dadosParaTransferencia) {
		transferenciaComContasPessoaFisicaService
				.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(dadosParaTransferencia);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Recebe dinheiro vindo de outra instituição financeira.")
	@PostMapping("/recebeDinheiroVindoOutraInstituicaoFinanceira")
	public ResponseEntity<?> recebeDinheiroVindoOutraInstituicaoFinanceira(
			@RequestBody DadosParaRecebimentoDinheiroVindoOutraInstituicaoFinanceira dadosParaRecebimentoDinheiro) {
		transferenciaComContasPessoaFisicaService
				.recebeDinheiroVindoOutraInstituicaoFinanceira(dadosParaRecebimentoDinheiro);
		return ResponseEntity.ok().build();
	}
}