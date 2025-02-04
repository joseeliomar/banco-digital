package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
public class ExtratoContaPessoaFisicaService extends ItemExtratoContaService {

	@Autowired
	private ItemExtratoContaPessoaFisicaRepository itemExtratoContaPessoaFisicaRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

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
		ItemExtratoContaPessoaFisica novoItemExtratoContaPessoaFisica = new ItemExtratoContaPessoaFisica(tipoConta,
				operacaoEfetuada, descricaoOperacao, dataHoraCadastro, bancoDestino, agenciaDestino, contaDestino,
				valor, cpfCliente);

		var itemExtratoContaPessoaFisicaSalvo = itemExtratoContaPessoaFisicaRepository
				.save(novoItemExtratoContaPessoaFisica);

		ContaDigitalPessoaFisicaDTO1Busca contaDigital = buscaContaDigitalClientePeloCpf(cpfCliente);
		notificaClienteSobreMovitacaoBancaria(contaDigital, novoItemExtratoContaPessoaFisica);

		return itemExtratoContaPessoaFisicaSalvo;
	}

	/**
	 * Notifica o cliente sobre a nova movimentação bancária.
	 * 
	 * @param contaDigital
	 * @param itemExtratoContaPessoaFisica
	 */
	private void notificaClienteSobreMovitacaoBancaria(ContaDigitalPessoaFisicaDTO1Busca contaDigital,
			ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica) {
		String telefoneMaisMensagem = contaDigital.getTelefone() + ";"
				+ criaMensagemNotificacaoSobreMovitacaoBancaria(contaDigital, itemExtratoContaPessoaFisica);

		kafkaTemplate.send("movimentacao-conta-pessoa-fisica-notificacao-topico", telefoneMaisMensagem);
	}

	/**
	 * Cria a mensagem de notificação sobre a moviação bancária.
	 * 
	 * @param contaDigital
	 * @param itemExtratoContaPessoaFisica
	 * @return a mensagem de notificação.
	 */
	private String criaMensagemNotificacaoSobreMovitacaoBancaria(ContaDigitalPessoaFisicaDTO1Busca contaDigital,
			ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica) {

		StringBuilder mensagem = new StringBuilder();
		mensagem.append("\nOlá ").append(contaDigital.getNomeCompleto()).append(", esperamos que esteja bem!\n")
				.append("Houve uma nova movimentação em sua conta e viemos te informar. Dados da movimentação:\n\n")
				.append("Dia: ")
				.append(Utils
						.obtemDataFormatadaSemHorario(itemExtratoContaPessoaFisica.getDataHoraCadastro().toLocalDate()))
				.append("\nHorário da movimentação: ")
				.append(Utils.obtemHoraFormatada(itemExtratoContaPessoaFisica.getDataHoraCadastro())).append(".\n")
				.append("Descrição: ").append(itemExtratoContaPessoaFisica.getDescricaoOperacao()).append(".\nValor: ")
				.append(itemExtratoContaPessoaFisica.getValor()).append(".\n\n")
				.append("Se você desconhece essa movimentação recomendamos acessar o app do JBank ou o nosso ")
				.append("internet bank para mais informações e se você desejar, entre em contato conosco por ")
				.append("meio do nosso telefone oficial presente em nossas plataformas oficiais.");
		return mensagem.toString();
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
		Optional<ItemExtratoContaPessoaFisica> itemExtratoContaPessoaFisicaOptional = itemExtratoContaPessoaFisicaRepository
				.findById(id);

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
	 * @param quantidadeDias quantidade de dias a ser utilizada na busca. O
	 *                       intervalo de busca será formado da seguinte forma: (dia
	 *                       atual - quantidade de dias especificada).
	 * @return o extrato.
	 */
	public ExtratoContaCorrenteDto geraExtratoContaCorrente(String cpfCliente, int quantidadeDias) {
		validaCpfCliente(cpfCliente);

		LocalDate dataFinalPeriodo = LocalDate.now();
		LocalDate dataInicialPeriodo = dataFinalPeriodo.minusDays(quantidadeDias);

		List<ItemExtratoContaPessoaFisica> itensExtratoContaCorrente = itemExtratoContaPessoaFisicaRepository
				.buscaItensExtrato(cpfCliente, TipoConta.CORRENTE, dataInicialPeriodo, dataFinalPeriodo);

		List<MovimentacaoDto> movimentacoes = new ArrayList<>();
		Object[] operacoesNasQuaisEntramDinheiroNaConta = new Operacao[] { Operacao.DEPOSITO,
				Operacao.TRANSFERENCIA_VINDA_DE_OUTRA_INSTITUICAO_FINANCEIRA,
				Operacao.TRANSFERENCIA_PARA_MESMA_INSTITUICAO_FINANCEIRA_ENTRADA_DINHEIRO };
		double totalEntradas = 0.0;
		double totalSaidas = 0.0;
		for (ItemExtratoContaPessoaFisica itemExtratoContaCorrente : itensExtratoContaCorrente) {
			LocalDateTime dataHoraCadastro = itemExtratoContaCorrente.getDataHoraCadastro();
			String dataFormatadaSemHora = Utils.obtemDataFormatadaSemHorario(dataHoraCadastro.toLocalDate());
			String horaFormatada = Utils.obtemHoraFormatada(dataHoraCadastro);
			Operacao operacaoEfetuada = itemExtratoContaCorrente.getOperacaoEfetuada();
			double valor = itemExtratoContaCorrente.getValor();

			movimentacoes.add(
					new MovimentacaoDto(dataFormatadaSemHora, horaFormatada, itemExtratoContaCorrente.getBancoDestino(),
							itemExtratoContaCorrente.getAgenciaDestino(), itemExtratoContaCorrente.getContaDestino(),
							operacaoEfetuada, itemExtratoContaCorrente.getDescricaoOperacao(), valor));

			if (Utils.primeiraOpcaoIgualAlgumaDemaisOpcoesEspecificadas(operacaoEfetuada,
					operacoesNasQuaisEntramDinheiroNaConta)) {
				totalEntradas += valor;
			} else {
				totalSaidas += valor;
			}
		}

		ContaDigitalPessoaFisicaDTO1Busca contaDigitalPessoaFisica = buscaContaDigitalClientePeloCpf(cpfCliente);

		validaSeEncontradaContaDigitalCliente(contaDigitalPessoaFisica);

		String dataInicialPeriodoFormatada = Utils.obtemDataFormatadaSemHorario(dataInicialPeriodo);
		String dataFinalPeriodoFormatada = Utils.obtemDataFormatadaSemHorario(dataFinalPeriodo);

		String periodo = dataInicialPeriodoFormatada + " a " + dataFinalPeriodoFormatada;
		double saldoFinalPeriodo = totalEntradas - totalSaidas;

		ExtratoContaCorrenteDto extratoContaCorrenteDto = new ExtratoContaCorrenteDto(periodo,
				contaDigitalPessoaFisica.getNomeCompleto(), cpfCliente, contaDigitalPessoaFisica.getAgencia(),
				contaDigitalPessoaFisica.getConta(), totalEntradas, totalSaidas, saldoFinalPeriodo, movimentacoes);

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
