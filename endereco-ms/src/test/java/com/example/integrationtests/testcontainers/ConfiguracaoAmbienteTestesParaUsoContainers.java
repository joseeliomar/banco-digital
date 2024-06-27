package com.example.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = ConfiguracaoAmbienteTestesParaUsoContainers.Initializer.class)
public class ConfiguracaoAmbienteTestesParaUsoContainers {

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.37");
		
		private static void startContainers() {
			Startables.deepStart(Stream.of(mysqlContainer)).join();
		}
		
		private static Map<String, Object> createConnectionConfiguration() {
			return Map.of(
					"spring.datasource.url", mysqlContainer.getJdbcUrl(),
					"spring.datasource.username", mysqlContainer.getUsername(),
					"spring.datasource.password", mysqlContainer.getPassword());
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
