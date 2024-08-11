package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ExtratoContaCorrenteDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.MovimentacaoDto;
import com.example.enumeration.Banco;
import com.example.enumeration.Operacao;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ItemExtratoContaPessoaFisica;
import com.example.repository.ItemExtratoContaPessoaFisicaRepository;
import com.example.utils.Utils;

@Service
public class ExtratoContaPessoaFisicaService extends ItemExtratoContaService{
	
	@Autowired
	private ItemExtratoContaPessoaFisicaRepository itemExtratoContaPessoaFisicaRepository;
	
	public ItemExtratoContaPessoaFisica insereItemExtratoContaPessoaFisica(
			ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto) {
		TipoConta tipoConta = itemExtratoContaPessoaFisicaInsercaoDto.getTipoContaDonaExtrato();
		Operacao operacaoEfetuada = itemExtratoContaPessoaFisicaInsercaoDto.getOperacaoEfetuada();
		String descricaoOperacao = itemExtratoContaPessoaFisicaInsercaoDto.getDescricaoOperacao();
		Banco bancoDestino = itemExtratoContaPessoaFisicaInsercaoDto.getBancoDestino();
		String agenciaDestino = itemExtratoContaPessoaFisicaInsercaoDto.getAgenciaDestino();
		String contaDestino = itemExtratoContaPessoaFisicaInsercaoDto.getContaDestino();
		double valor = itemExtratoContaPessoaFisicaInsercaoDto.getValor();
		String cpfCliente = itemExtratoContaPessoaFisicaInsercaoDto.getCpfCliente();
		
		super.validaTipoConta(tipoConta);
		super.validaOperacaoEfetuada(operacaoEfetuada);
		super.validaDescricaoOperacao(descricaoOperacao);
		super.validaBancoDestino(bancoDestino);
		super.validaAgenciaDestino(agenciaDestino);
		super.validaContaDestino(contaDestino);
		validaCpfCliente(cpfCliente);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ItemExtratoContaPessoaFisica novoItemExtratoContaPessoaFisica = new ItemExtratoContaPessoaFisica(
				tipoConta, operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino,
				agenciaDestino, contaDestino, valor, cpfCliente);
		return itemExtratoContaPessoaFisicaRepository.save(novoItemExtratoContaPessoaFisica);
	}
	
	private void validaCpfCliente(String cpfCliente) {
		if (cpfCliente == null || cpfCliente.isBlank()) {
			throw new ValidacaoException("O CPF do cliente não foi informado.", HttpStatus.BAD_REQUEST);
		}
		if (cpfCliente.length() < 11) {
			throw new ValidacaoException("O CPF do cliente está com menos de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cpfCliente.length() > 11) {
			throw new ValidacaoException("O CPF do cliente está com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public void removeItemExtratoContaPessoaFisica(Long id) {
		Optional<ItemExtratoContaPessoaFisica> itemExtratoContaPessoaFisicaOptional = itemExtratoContaPessoaFisicaRepository.findById(id);
		
		ItemExtratoContaPessoaFisica contaDigitalPessoaFisicaSalvaBancoDados = itemExtratoContaPessoaFisicaOptional
				.orElseThrow(() -> new ValidacaoException(
						"Não foi encontrado um item de extrato de conta de pessoa física com o código informado.",
						HttpStatus.BAD_REQUEST));
		
		itemExtratoContaPessoaFisicaRepository.delete(contaDigitalPessoaFisicaSalvaBancoDados);
	}

	/**
	 * Gera o extrato da conta corrente.
	 * 
	 * @param cpfCliente
	 * @param quantidadeDias quantidade de dias a ser utilizada na busca. 
	 * 	O intervalo de busca será formado da seguinte forma: (dia atual - quantidade de dias especificada).
	 * @return o extrato.
	 */
	public ExtratoContaCorrenteDto geraExtratoContaCorrente(String cpfCliente, int quantidadeDias) {
		validaCpfCliente(cpfCliente);
		
		LocalDateTime dataFinalPeriodo = LocalDate.now().atStartOfDay();
		LocalDateTime dataInicialPeriodo = dataFinalPeriodo.minusDays(quantidadeDias);
		
		List<ItemExtratoContaPessoaFisica> itensExtratoContaCorrente = itemExtratoContaPessoaFisicaRepository
				.buscaItensExtrato(cpfCliente, TipoConta.CORRENTE, dataInicialPeriodo, dataFinalPeriodo);
		
		List<MovimentacaoDto> movimentacoes = new ArrayList<>();
		Object[] operacoesNasQuaisEntramDinheiroParaConta = new Operacao[] { Operacao.DEPOSITO,
				Operacao.TRANSFERENCIA_DE_OUTRA_INSTITUICAO_FINANCEIRA };
		double totalEntradas = 0.0;
		double totalSaidas = 0.0;
		for (ItemExtratoContaPessoaFisica itemExtratoContaCorrente : itensExtratoContaCorrente) {
			LocalDateTime dataHoraCadastro = itemExtratoContaCorrente.getDataHoraCadastro();
			String dataFormatadaSemHora = Utils.obtemDataFormatadaSemHorario(dataHoraCadastro);
			String horaFormatada = Utils.obtemHoraFormatada(dataHoraCadastro);
			Operacao operacaoEfetuada = itemExtratoContaCorrente.getOperacaoEfetuada();
			double valor = itemExtratoContaCorrente.getValor();
			
			movimentacoes.add(new MovimentacaoDto(
					dataFormatadaSemHora, 
					horaFormatada, 
					itemExtratoContaCorrente.getBancoDestino(),
					itemExtratoContaCorrente.getAgenciaDestino(), 
					itemExtratoContaCorrente.getContaDestino(),
					operacaoEfetuada, 
					itemExtratoContaCorrente.getDescricaoOperacao(),
					valor));
			
			if (Utils.primeiraOpcaoIgualAlgumaDemaisOpcoesEspecificadas(operacaoEfetuada,
					operacoesNasQuaisEntramDinheiroParaConta)) {
				totalEntradas += valor;
			} else {
				totalSaidas += valor;
			}
		}
		
		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalClientePeloCpf(cpfCliente);
		
		validaSeEncontradaContaDigitalCliente(contaDigitalPessoaFisica);
		
		ExtratoContaCorrenteDto extratoContaCorrenteDto = new ExtratoContaCorrenteDto(contaDigitalPessoaFisica.getNomeCompleto(), cpfCliente,
				contaDigitalPessoaFisica.getAgencia(), contaDigitalPessoaFisica.getConta(), totalEntradas, totalSaidas,
				movimentacoes);
		
		return extratoContaCorrenteDto;
	}
	
	/**
	 * Valida se encontrada a conta digital do cliente.
	 * 
	 * @param contaDigitalClienteQueQuerTransferirDinheiro
	 */
	private void validaSeEncontradaContaDigitalCliente(ContaDigitalPessoaFisicaDTO1Busca contaDigitalCliente) {
		if (contaDigitalCliente == null) {
			throw new ValidacaoException(
					"Não foi encontrada a conta digital do cliente que quer transferir o dinheiro.",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Busca conta digital de cliente pelo CPF.
	 * 
	 * @param cpfCliente
	 * @return a conta digital de cliente.
	 */
	private ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalClientePeloCpf(String cpfCliente) {
		ResponseEntity<ContaDigitalPessoaFisicaDTO1Busca> respostaBuscaContaDigitalPessoaFisica = contaDigitalClienteMsFeignClient
				.buscaContaDigitalPessoaFisica(cpfCliente);

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = respostaBuscaContaDigitalPessoaFisica.getBody();
		return contaDigitalPessoaFisica;
	}
}
