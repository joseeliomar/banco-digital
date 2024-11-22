package br.com.jbank.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.jbank.service.IAService;
import br.com.jbank.service.OllamaService;
import br.com.jbank.service.SpringAIService;

@Configuration
public class ConfigIAService {

	@Value("${ia-service}")
	private String configuracaoIAService;

	/**
	 * Definine através de configuração qual service de IA será retonada.
	 * 
	 * @return uma service de IA.
	 */
	@Bean
	@Qualifier("selectedIAService")
	IAService retornaIAServive() {
		IAService iaService = new OllamaService(); // service padrão caso não seja informado um valor válido ou nenhum
													// valor para a configuração

		if ("springAI".equals(configuracaoIAService)) {
			iaService = new SpringAIService();
		}

		return iaService;
	}
}
