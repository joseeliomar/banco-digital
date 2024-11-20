package br.com.jbank.bot.telegram;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbank.dto.TransferenciaEntreContasMesmoClienteDto;
import br.com.jbank.enumeration.Operacao;
import br.com.jbank.modelo.ConversationUserWithBot;
import br.com.jbank.modelo.Message;
import br.com.jbank.openai.enumeration.Role;
import br.com.jbank.openfeign.feignclient.TransferenciaFeignClient;
import br.com.jbank.service.ConversaUsuarioComBotService;
import br.com.jbank.service.OllamaService;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	@Value("${telegram.bot.username}")
	private String botUsername;

	@Autowired
	private OllamaService iAService;

	@Autowired
	private ConversaUsuarioComBotService conversationService;

	@Autowired
	private TransferenciaFeignClient transferenciaFeignClient;

	@Autowired
	private ObjectMapper objectMapper;

	public TelegramBot(@Value("${telegram.bot.token}") String botToken) {
		super(botToken);
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String novaMensagemUsuario = update.getMessage().getText();
			String telegramChatId = update.getMessage().getChatId().toString();

			ConversationUserWithBot conversaAtualComBot = buscaConversa(telegramChatId);
			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.USER.getTexto(), novaMensagemUsuario);

			String respostaParaUsuario;
			try {
				respostaParaUsuario = iAService.processarMensagem(conversaAtualComBot.getMessages());

				if (respostaParaUsuario.contains("OK_TUDO_PRONTO_PROCESSAMENTO_DADOS")) {
					Message ultimaMensagemBota = conversaAtualComBot.getMessages().get(conversaAtualComBot.getMessages().size() - 1);
					ultimaMensagemBota.setContent(ultimaMensagemBota.getContent().replaceAll("OK_TUDO_PRONTO_PROCESSAMENTO_DADOS", ""));
					
					TransferenciaEntreContasMesmoClienteDto dadosParaTransferencia = geraDtoParaTransferencia(conversaAtualComBot);
					// Converte a resposta em um objeto Transferencia
//					ObjectMapper objectMapper = new ObjectMapper();
//                    Transferencia transferencia = objectMapper.readValue(response, Transferencia.class);
					// realiza aqui a transferência

					Operacao operacao = dadosParaTransferencia.tipoTransferencia();

					HttpStatusCode statusCode = null;
					if (operacao.equals(Operacao.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO)) {
						statusCode = this.transferenciaFeignClient
								.efetuaTransferenciaEntreContasClientesDiferentesDesseBanco(null).getStatusCode();
						respostaParaUsuario += "A transferência ocorreu com sucesso";
					} else if (operacao.equals(Operacao.TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE)) {
						statusCode = this.transferenciaFeignClient.efetuaTransferenciaEntreContasMesmoCliente(null)
								.getStatusCode();
					} else if (operacao
							.equals(Operacao.TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES)) {
						statusCode = this.transferenciaFeignClient
								.efetuaTransferenciaEntreContasClientesInstituicoesFinanceirasDiferentes(null)
								.getStatusCode();
					} else {
						respostaParaUsuario += "Escolha um os tipos de transferência que te informei.";
					}

					if (statusCode != null && statusCode.is2xxSuccessful()) {
						respostaParaUsuario += "A transferência ocorreu com sucesso!";
					}
				}
			} catch (Exception e) {
				respostaParaUsuario = "Desculpe, ocorreu um erro ao processar sua solicitação. Tente novamente.";
				e.printStackTrace();
			}

			adicionaNovaMensagemNaConversa(conversaAtualComBot, Role.ASSISTANT.getTexto(), respostaParaUsuario);
			conversationService.salvaConversa(conversaAtualComBot);

			enviaRespostaBot(telegramChatId, respostaParaUsuario);
		}
	}

	private TransferenciaEntreContasMesmoClienteDto geraDtoParaTransferencia(
			ConversationUserWithBot conversaUsuarioComBot) throws JsonMappingException, JsonProcessingException {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		String promptParaIA = "Agora que o cliente já informou os dados da transferência e confirmou que deseja "
				+ "prosseguir com a transferência bancária: gere um JSON com o CPF do usuário, o valor da "
				+ "transferência, o tipo de transferência e o tipo da conta de origem do dinheiro (origem do dinheiro)."
				+ "- Sobre o CPF: O CPF é 1234567890."
				+ "- Sobre o valor informado: no JSON o valor deve ficar no formado de valores que os americanos usam. "
				+ "Exemplo: deve ser 100000.00 ao invés de 100000,00."
				+ "- Sobre o tipo de transferência informado: no JSON o valor da propriedade relacionada com"
				+ "o tipo de transferência deve ser TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE para o tipo de transferência 1,"
				+ "ser TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_DIFERENTES_DESSE_BANCO para o tipo de transferência 2 e ser"
				+ "TRANSFERENCIA_ENTRE_CONTAS_CLIENTES_INSTITUICOES_FINANCEIRAS_DIFERENTES para o tipo de transferência 3."
				+ "- Sobre o tipo da conta de origem do dinheiro: o valor da propriedade relacionada com "
				+ "o tipo da conta de origem do dinheiro deve ser CORRENTE para a opção \"1 - Conta corrente\" "
				+ "e ser POUPANCA para a opção \"2 - Conta poupança\""
				+ "O JSON dever ser gerado nesse formato e com essas propriedades:" + "{\r\n"
				+ "    \"cpfCliente\": \"12345678901\",\r\n"
				+ "    \"tipoTransferencia\": \"TRANSFERENCIA_ENTRE_CONTAS_MESMO_CLIENTE\",\r\n"
				+ "    \"valorTransferencia\": 400.00,\r\n" + "    \"tipoContaOrigemDinheiro\": \"CORRENTE\"\r\n" + "}"
				+ "A sua resposta deve ser apenas o JSON que eu te pedi.";

		adicionaNovaMensagemNaConversa(conversaUsuarioComBot, Role.SYSTEM.getTexto(), promptParaIA);
		String json = iAService.processarMensagem(conversaUsuarioComBot.getMessages());

		TransferenciaEntreContasMesmoClienteDto dadosParaTransferencia = objectMapper.readValue(json,
				TransferenciaEntreContasMesmoClienteDto.class);

		return dadosParaTransferencia;
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
			String promptParaIA = "Você, como assistente virtual do JBank, deve começar a conversa com uma saudação amigável, explicando seu papel e pedindo para o cliente escolher o tipo de transferência que deseja realizar.\r\n"
					+ "Exemplo de início da conversa:\r\n" + "\r\n"
					+ "\"Olá! Eu sou o assistente virtual do JBank e estou aqui para ajudá-lo a realizar a sua transferência. Primeiro, por favor, me diga qual tipo de transferência você gostaria de fazer. Você tem as seguintes opções:\r\n"
					+ "1 - Transferência entre suas contas no JBank (conta corrente para conta poupança ou conta poupança para conta corrente, sendo contas do mesmo cliente).\r\n"
					+ "2 - Transferência entre contas de clientes diferentes do JBank (de conta corrente de um cliente para conta corrente de outro cliente ou de conta poupança para conta corrente de outro cliente).\r\n"
					+ "3 - Transferência para outra instituição financeira (da sua conta do JBank para a conta de outro cliente em um banco diferente).\r\n"
					+ "Por favor, escolha uma das opções acima, digitando o número correspondente.\"\r\n" + "\r\n"
					+ "Coleta de Informações - Caso o Cliente Escolha a Opção 1 (Transferência entre suas contas no JBank):\r\n"
					+ "\r\n"
					+ "Se o cliente escolher a opção 1, pergunte o valor da transferência e a origem do dinheiro.\r\n"
					+ "Informe ao cliente as opções de origem (conta corrente ou conta poupança).\r\n"
					+ "Exemplo para a Opção 1:\r\n" + "\r\n"
					+ "\"Você escolheu a opção 1: Transferência entre suas contas no JBank.\r\n"
					+ "Agora, por favor, me informe o valor da transferência. Qual valor você gostaria de transferir?\"\r\n"
					+ "\r\n" + "Depois que o cliente fornecer o valor, pergunte sobre a origem do dinheiro.\r\n"
					+ "\"Agora, por favor, escolha a origem do dinheiro:\r\n" + "1 - Conta corrente\r\n"
					+ "2 - Conta poupança\r\n" + "Por favor, digite o número correspondente à origem do dinheiro.\"\r\n"
					+ "\r\n"
					+ "Coleta de Informações - Caso o Cliente Escolha a Opção 2 (Transferência entre contas de clientes diferentes):\r\n"
					+ "\r\n"
					+ "Se o cliente escolher a opção 2, além de solicitar o valor, pergunte o número da conta de destino e o número da conta de origem.\r\n"
					+ "Exemplo para a Opção 2:\r\n" + "\r\n"
					+ "\"Você escolheu a opção 2: Transferência entre contas de clientes diferentes do JBank.\r\n"
					+ "Agora, por favor, me informe o valor da transferência. Qual valor você gostaria de transferir?\"\r\n"
					+ "\r\n"
					+ "Depois que o cliente fornecer o valor, pergunte pelo número da conta de destino e da conta de origem.\r\n"
					+ "\"Agora, por favor, informe o número da conta de destino (número da conta do cliente para quem você está transferindo o dinheiro).\"\r\n"
					+ "\r\n" + "\"Por fim, por favor, informe o número da sua conta de origem.\"\r\n" + "\r\n"
					+ "Coleta de Informações - Caso o Cliente Escolha a Opção 3 (Transferência para outra instituição financeira):\r\n"
					+ "\r\n"
					+ "Se o cliente escolher a opção 3, além do valor da transferência, você deve solicitar o número da conta de destino em outra instituição e o nome da instituição financeira.\r\n"
					+ "Exemplo para a Opção 3:\r\n" + "\r\n"
					+ "\"Você escolheu a opção 3: Transferência para outra instituição financeira.\r\n"
					+ "Agora, por favor, me informe o valor da transferência. Qual valor você gostaria de transferir?\"\r\n"
					+ "\r\n"
					+ "Depois que o cliente fornecer o valor, pergunte pelo número da conta de destino em outra instituição e o nome da instituição financeira.\r\n"
					+ "\"Agora, por favor, informe o número da sua conta de origem no JBank.\"\r\n" + "\r\n"
					+ "\"Agora, me informe o número da conta de destino (número da conta do cliente na outra instituição financeira).\"\r\n"
					+ "\r\n" + "\"Por fim, por favor, me diga o nome da instituição financeira de destino.\"\r\n"
					+ "\r\n" + "Resumo e Confirmação:\r\n" + "\r\n"
					+ "Após coletar todas as informações necessárias, apresente um resumo claro e preciso dos dados fornecidos, com base no tipo de transferência escolhido.\r\n"
					+ "Pergunte ao cliente se ele deseja confirmar a transferência, oferecendo a opção de prosseguir ou corrigir algum detalhe.\r\n"
					+ "Exemplo de Resumo:\r\n" + "\r\n"
					+ "\"Ótimo! Agora que temos todas as informações necessárias, vou resumir os detalhes da sua transferência:\r\n"
					+ "Tipo de Transferência: Transferência entre suas contas do JBank (conta corrente para conta poupança ou conta poupança para conta corrente, sendo essas contas do mesmo cliente).\r\n"
					+ "Valor: R$100,00\r\n" + "Origem do Dinheiro: Conta corrente.\r\n" + "\r\n"
					+ "Deseja confirmar e prosseguir com a transferência?\r\n"
					+ "Por favor, digite 'sim' para confirmar ou 'não' se precisar corrigir algum detalhe.\"\r\n"
					+ "\r\n" + "Instruções Finais:\r\n" + "\r\n"
					+ "Se o cliente confirmar com \"sim\", a IA deve proceder com a finalização da transferência.\r\n"
					+ "Se o cliente responder \"não\", a IA deve pedir para o cliente corrigir as informações e iniciar o processo novamente.\r\n"
					+ "Exemplo de Correção:\r\n" + "\r\n"
					+ "\"Entendido! Vamos começar novamente. Qual tipo de transferência você gostaria de realizar?\"\r\n"
					+ "\r\n"
					+ "Uma outra instrução é: se você já tiver obtido todas as informações necessárias para a transferência bancária atual que o cliente deseja realizar "
					+ "e o cliente já tiver confirmado que deseja fazer a transferência atual, "
					+ " no final da sua resposta você deve acrescentar a string \"OK_TUDO_PRONTO_PROCESSAMENTO_DADOS\" para que o sistema saiba que está tudo pronto para realizar a transferência";

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
	 * @param funcao                a função do personagem da conversa.
	 * @param novaMensagemUsuario
	 */
	private void adicionaNovaMensagemNaConversa(ConversationUserWithBot conversaUsuarioComBot, String funcao,
			String novaMensagemUsuario) {
		conversaUsuarioComBot.getMessages().add(new Message(funcao, novaMensagemUsuario, Instant.now()));
	}

	/**
	 * Envia a resposta do bot para o usuário
	 * 
	 * @param telegramChatId
	 * @param respostaBotParaUsuario
	 */
	private void enviaRespostaBot(String telegramChatId, String respostaBotParaUsuario) {
		SendMessage novoEnvioMensagem = new SendMessage();
		novoEnvioMensagem.setChatId(telegramChatId);
		novoEnvioMensagem.setText(respostaBotParaUsuario);

		try {
			execute(novoEnvioMensagem);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

}
