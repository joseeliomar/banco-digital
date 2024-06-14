package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.integrationtests.testcontainers.ConfiguracaoAmbienteTestesParaUsoContainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContaCorrentePoupancaMsApplicationTests extends ConfiguracaoAmbienteTestesParaUsoContainers {

	@Test
	void contextLoads() {
	}

}
