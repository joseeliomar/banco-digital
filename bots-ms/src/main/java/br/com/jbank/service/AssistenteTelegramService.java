package br.com.jbank.service;

import java.time.Instant;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbank.dto.ContaPessoaFisicaBuscaDto1;
import br.com.jbank.dto.DadosParaTransferenciaBancaria;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto;
import br.com.jbank.dto.DadosParaTransferenciaEntreContasMesmoClienteDto;
import br.com.jbank.enumeration.NomeArquivoPrompt;
import br.com.jbank.enumeration.TipoTransferencia;
import br.com.jbank.enumeration.StatusOkProcessamentoOperacao;
import br.com.jbank.model.ConversationUserWithBot;
import br.com.jbank.model.Message;
import br.com.jbank.openai.enumeration.Role;
import br.com.jbank.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;
import br.com.jbank.openfeign.feignclient.TransferenciaFeignClient;
import br.com.jbank.prompt.CarregadorPrompts;
import br.com.jbank.utils.Utils;

@Service
public class AssistenteTelegramService {

	private static final String MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO = 
			"Caso o problema persista, por favor, utilize o nosso aplicativo ou o nosso internet "
			+ "bank. Problemas acontecem, mas estamos sempre trabalhando para resolve-los e para "
			+ "melhorar os nossos produtos e serviços. Nos desculpe pelo transtorno.";

	@Autowired
	@Qualifier("selectedIAService")
	private IAService iAService;

	@Autowired
	private ConversationUserWithBotService conversationService;

	@Autowired
	private TransferenciaFeignClient transferenciaFeignClient;

	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;

	@Autowired
	private CarregadorPrompts carregadorPrompts;

	private ObjectMapper objectMapper;

	public AssistenteTelegramService() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * Processa a mensagem recebida.
	 * 
	 * @param exchange
	 */
	public void processaMensagemRecebida(Exchange exchange) {
		String novaMensagemUsuario = exchange.getIn().getBody(String.class);
		if (novaMensagemUsuario != null && !novaMensagemUsuario.isEmpty()) {
			String telegramChatId = exchange.getIn().getHeader("CamelTelegramChatId", String.class);

			ConversationUserWithBot conversaAtualComBot = buscaConversa(telegramChatId);
			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.USER.getTexto(), novaMensagemUsuario);

			String respostaParaUsuario = null;
			String respostaModelo = null;
			try {
				respostaModelo = iAService.processarMensagens(conversaAtualComBot.getMessages());

				if (respostaModelo
						.contains(StatusOkProcessamentoOperacao.STATUS_PARA_PROCESSAMENTO_TRANSFERENCIA.getValor())) {
					respostaParaUsuario = executaTransferenciaBancaria(conversaAtualComBot);
					adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.SYSTEM.getTexto(), respostaParaUsuario);
				} else if (respostaModelo
						.contains(StatusOkProcessamentoOperacao.STATUS_PARA_CONSULTA_SALDO.getValor())) {
					respostaParaUsuario = executaConsultaSaldo(conversaAtualComBot);
					adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.SYSTEM.getTexto(), respostaParaUsuario);
				} else {
					respostaParaUsuario = respostaModelo;
					adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.ASSISTANT.getTexto(), respostaModelo);
				}
			} catch (Exception e) {
				respostaParaUsuario = "Desculpe, ocorreu um erro ao processar sua solicitação. Tente novamente.";
				adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.SYSTEM.getTexto(), respostaModelo);
				e.printStackTrace();
			}

			conversationService.salvaConversa(conversaAtualComBot);
			exchange.getMessage().setBody(respostaParaUsuario);
		}
	}

	/**
	 * Executa a consulta do saldo da conta do cliente.
	 * 
	 * @param conversaAtualComBot
	 * @return resposta sobre a consulta do saldo, que pode ser tanto uma mensagem
	 *         informando o saldo atual quanto alguma outra mensagem que envolve
	 *         esse processo de consulta.
	 */
	private String executaConsultaSaldo(ConversationUserWithBot conversaAtualComBot) {
		DadosParaConsultaSaldo dadosParaConsultaSaldo = obtemDadosConsultaSaldo(conversaAtualComBot);

		if (dadosParaConsultaSaldo == null) {
			return "Ocorreu algum problema ao tentarmos obter os dados necessário para a consultado do saldo. "
					+ MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}

		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaAPI = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaFisica(dadosParaConsultaSaldo.cpfCliente(), dadosParaConsultaSaldo.tipoConta());

		HttpStatusCode statusCode = respostaAPI.getStatusCode();

		if (statusCode != null && statusCode.is2xxSuccessful()) {
			ContaPessoaFisicaBuscaDto1 contaPessoaFisica = respostaAPI.getBody();
			return "O saldo atual da sua conta " + contaPessoaFisica.getTipoConta().getNome().toLowerCase() + " é de "
					+ Utils.formataValorReais(contaPessoaFisica.getSaldo()) + ".";
		} else {
			return "Ocorreu algum problema durante a consultado do saldo. Por favor, solicite "
					+ "novamente mais tarde que o último pedido de consulta de saldo seja processsado novamente. "
					+ MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}
	}

	/**
	 * Obtem os dados necessários para a consulta do saldo da conta desejada.
	 * 
	 * @param conversaUsuarioComBot
	 * @return os dados necessários para a consultado do saldo.
	 */
	private DadosParaConsultaSaldo obtemDadosConsultaSaldo(ConversationUserWithBot conversaUsuarioComBot) {
		String promptParaIA = carregadorPrompts
				.getPrompt(NomeArquivoPrompt.PROMPT_PARA_EXTRACAO_DADOS_NECESSARIO_CONSULTA_SALDO.getNome());

		adicionaNovaMensagemNaConversa(conversaUsuarioComBot, Role.SYSTEM.getTexto(), promptParaIA);
		String json = iAService.processarMensagens(conversaUsuarioComBot.getMessages());

		DadosParaConsultaSaldo dadosTransferenciaBancaria = null;
		try {
			dadosTransferenciaBancaria = objectMapper.readValue(json, DadosParaConsultaSaldo.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return dadosTransferenciaBancaria;
	}

	/**
	 * Executa a transferência bancária desejada.
	 * 
	 * @param conversaAtualComBot
	 * @return resposta sobre a transferência, que pode ser tanto uma mensagem
	 *         informando que a transferência ocorreu com sucesso quanto alguma
	 *         outra mensagem que envolve esse processo de transferência.
	 */
	private String executaTransferenciaBancaria(ConversationUserWithBot conversaAtualComBot) {
		DadosExtraidosTransferenciaBancaria dadosExtraidosTransferenciaBancaria = obtemDadosTransferencia(
				conversaAtualComBot);
		DadosParaTransferenciaBancaria dadosParaTransferenciaBancaria = dadosExtraidosTransferenciaBancaria
				.dadosParaTransferenciaBancaria();

		if (dadosExtraidosTransferenciaBancaria == null || dadosParaTransferenciaBancaria == null) {
			return "Ocorreu algum problema ao tentarmos obter os dados necessário para a transferência. "
					+ MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}

		HttpStatusCode statusCode = null;
		TipoTransferencia operacao = dadosExtraidosTransferenciaBancaria.tipoTransferencia();
		if (operacao.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO)) {
			statusCode = efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(dadosParaTransferenciaBancaria);
		} else if (operacao.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE)) {
			statusCode = efetuaTransferenciaEntreContasMesmoCliente(dadosParaTransferenciaBancaria);
		} else if (operacao
				.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES)) {
			statusCode = efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
					dadosParaTransferenciaBancaria);
		} else {
			return "Desculpe, ocorreu algum problema e não conseguimos extrair a modalidade de transferência. "
					+ MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}

		if (statusCode != null && statusCode.is2xxSuccessful()) {
			return "A transferência ocorreu com sucesso!";
		} else {
			return "Ocorreu algum problema durante a transferência. Por favor, solicite "
					+ "novamente mais tarde que o último pedido de transferência seja processsado novamente. "
					+ MENSAGEM_DESCULPA_PELO_PROBLEMA_OCORRIDO;
		}
	}

	/**
	 * Efetua transferência entre contas de cliente de instituições financeiras
	 * diferentes.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(
				(DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto) dadosParaTransferencia)
				.getStatusCode();
	}

	/**
	 * Efetua transferência entre contas do mesmo cliente, ou seja, da conta
	 * corrente para a conta poupança, mas também da conta poupança para a conta
	 * corrente.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasMesmoCliente(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient.efetuaTransferenciaEntreContasMesmoCliente(
				(DadosParaTransferenciaEntreContasMesmoClienteDto) dadosParaTransferencia).getStatusCode();
	}

	/**
	 * Efetua transferência entre contas de clientes diferentes desse banco.
	 * 
	 * @param dadosParaTransferencia
	 * @return o código de status HTTP que foi retornado ao tentar efetuar a
	 *         transferência via API.
	 */
	private HttpStatusCode efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
			DadosParaTransferenciaBancaria dadosParaTransferencia) {
		return this.transferenciaFeignClient
				.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(
						(DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto) dadosParaTransferencia)
				.getStatusCode();
	}

	/**
	 * Obtem os dados necessários para a transferência bancária desejada.
	 * 
	 * @param conversaUsuarioComBot
	 * @return
	 */
	private DadosExtraidosTransferenciaBancaria obtemDadosTransferencia(ConversationUserWithBot conversaUsuarioComBot) {
		String promptParaIA = carregadorPrompts
				.getPrompt(NomeArquivoPrompt.PROMPT_PARA_EXTRACAO_DADOS_NECESSARIOS_TRANSFERENCIA_BANCARIA.getNome());

		adicionaNovaMensagemNaConversa(conversaUsuarioComBot, Role.SYSTEM.getTexto(), promptParaIA);
		String json = iAService.processarMensagens(conversaUsuarioComBot.getMessages());

		try {
			Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			String tipoTransferencia = (String) map.get("tipoTransferencia");

			DadosParaTransferenciaBancaria dadosTransferenciaBancaria = null;
			if (tipoTransferencia.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasMesmoClienteDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE, dadosTransferenciaBancaria);
			} else if (tipoTransferencia
					.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasClientesDiferentesDesseBancoDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO, dadosTransferenciaBancaria);
			} else if (tipoTransferencia
					.equals(TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES.name())) {
				dadosTransferenciaBancaria = objectMapper.convertValue(map,
						DadosParaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentesDto.class);
				
				return new DadosExtraidosTransferenciaBancaria(
						TipoTransferencia.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES, dadosTransferenciaBancaria);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Busca a conversa no banco de dados pelo ID do chat. Se a conversa ainda não
	 * existir é criada uma nova conversa e ela é retornada.
	 * 
	 * @param telegramChatId
	 * @return conversa salva no banco ou uma nova caso ainda não exista no banco de
	 *         dados uma conversa com o ID do chat que foi informado.
	 */
	private ConversationUserWithBot buscaConversa(String telegramChatId) {
		ConversationUserWithBot conversationUserWithBot = conversationService
				.buscaConversaPeloIdChatTelegram(telegramChatId).orElse(null);

		if (conversationUserWithBot == null) {
			String promptParaIA = carregadorPrompts.getPrompt(NomeArquivoPrompt.PROMPT_INICIAL.getNome());

			ConversationUserWithBot novaConversa = new ConversationUserWithBot(telegramChatId, Instant.now());
			adicionaNovaMensagemNaConversa(novaConversa, Role.SYSTEM.getTexto(), promptParaIA);
			return novaConversa;
		}

		return conversationUserWithBot;
	}

	/**
	 * Adiciona uma nova mensagem a conversa.
	 * 
	 * @param conversaUsuarioComBot
	 * @param papel
	 * @param novaMensagemUsuario
	 */
	private void adicionaNovaMensagemNaConversa(ConversationUserWithBot conversaUsuarioComBot, String papel,
			String novaMensagemUsuario) {
		conversaUsuarioComBot.getMessages().add(new Message(papel, novaMensagemUsuario, Instant.now()));
	}

}
