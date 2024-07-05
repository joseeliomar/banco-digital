package com.example.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = ConfiguracaoAmbienteTestesParaUsoContainers.Initializer.class)
public class ConfiguracaoAmbienteTestesParaUsoContainers {

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3");
		
		private static void startContainers() {
			Startables.deepStart(Stream.of(postgreSQLContainer)).join();
		}
		
		private static Map<String, Object> createConnectionConfiguration() {
			return Map.of(
					"spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username", postgreSQLContainer.getUsername(),
					"spring.datasource.password", postgreSQLContainer.getPassword());
		}
		
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment configurableEnvironment = applicationContext.getEnvironment();
			MapPropertySource testcontainers = new MapPropertySource("testcontainers", createConnectionConfiguration());
			configurableEnvironment.getPropertySources().addFirst(testcontainers);
		}
	}
}
