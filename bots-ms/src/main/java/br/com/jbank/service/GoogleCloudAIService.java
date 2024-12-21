package br.com.jbank.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.api.SafetySetting.HarmBlockThreshold;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import br.com.jbank.model.Message;
import br.com.jbank.openai.enumeration.Role;

public class GoogleCloudAIService implements IAService {

	@Value("${google-cloud.project-id}")
	private String projectId;

	@Value("${google-cloud.location}")
	private String location;

	@Value("${google-cloud.ia.model}")
	private String model;

	@Override
	public Optional<String> processarMensagens(List<Message> mensagensConversa) {
		String resposta = null;
		try (VertexAI vertexAi = new VertexAI(this.projectId, this.location);) {
			resposta = geraRespostaModelo(mensagensConversa, vertexAi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.of(resposta);
	}

	/**
	 * Gera a resposta do modelo.
	 * 
	 * @param mensagensConversa
	 * @param vertexAi
	 * @return
	 * @throws IOException
	 */
	private String geraRespostaModelo(List<Message> mensagensConversa, VertexAI vertexAi) throws IOException {
		GenerativeModel model = criaModelo(vertexAi);
		List<Content> conteudoConversa = ConverteEstruturaMensagens(mensagensConversa);
		
		GenerateContentResponse generateContentResponse = model.generateContent(conteudoConversa);
		
		return converteRespostaParaString(generateContentResponse);
	}

	/**
	 * Converte a resposta para String.
	 * 
	 * @param generateContentResponse
	 * @return String com a resposta do modelo.
	 */
	private String converteRespostaParaString(GenerateContentResponse generateContentResponse) {
		StringBuilder resposta = new StringBuilder();
		generateContentResponse.getCandidatesList()
				.forEach(c -> c.getContent().getPartsList().forEach(p -> resposta.append(p.getText())));
		return resposta.toString();
	}

	/**
	 * Converte a estrutura das mensagens para um formato compatível com o esperado pela API do Google Cloud.
	 * 
	 * @param mensagensConversa
	 * @return mensagens dentro de uma nova estrutura.
	 */
	private List<Content> ConverteEstruturaMensagens(List<Message> mensagensConversa) {
		String roleAssistant = Role.ASSISTANT.getTexto();
		List<Content> conteudoConversa = mensagensConversa.stream().map(e -> ContentMaker
				.forRole(e.getRole().equals(roleAssistant) ? "model" : "user").fromString(e.getContent()))
				.collect(Collectors.toList());
		return conteudoConversa;
	}

	/**
	 * Cria o modelo generativo.
	 * 
	 * @param vertexAi
	 * @return modelo generativo.
	 */
	private GenerativeModel criaModelo(VertexAI vertexAi) {
		GenerationConfig generationConfig = GenerationConfig.newBuilder().setMaxOutputTokens(8192)
				.setTemperature(0.1F).setTopP(0.95F).build();
		List<SafetySetting> safetySettings = Arrays.asList(
				SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
						.setThreshold(HarmBlockThreshold.BLOCK_NONE).build(),
				SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
						.setThreshold(HarmBlockThreshold.BLOCK_NONE).build(),
				SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
						.setThreshold(HarmBlockThreshold.BLOCK_NONE).build(),
				SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
						.setThreshold(HarmBlockThreshold.BLOCK_NONE).build());
		return new GenerativeModel.Builder().setModelName(this.model)
				.setVertexAi(vertexAi).setGenerationConfig(generationConfig).setSafetySettings(safetySettings)
				.build();
	}
}
