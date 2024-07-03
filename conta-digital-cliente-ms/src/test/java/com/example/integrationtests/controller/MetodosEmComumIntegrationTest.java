package com.example.integrationtests.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.example.utils.Utils;

@Component
public class MetodosEmComumIntegrationTest {
	
	@Autowired
    private DiscoveryClient discoveryClient;

	/**
	 * Retorna uma porta do microsserviço especificado.
	 * 
	 * @param nomeMicrosservico Nome presente no arquivo application do microsserviço do qual se deseja obter uma porta.
	 * @return uma porta do microsserviço.
	 */
	public int obterUmaPortaMicrosservico(String nomeMicrosservico) {
		if (Utils.stringNulaVaziaOuEmBranco(nomeMicrosservico)) {
			throw new RuntimeException("Não foi informado o nome do microsserviço");
		}

		List<ServiceInstance> instancias = discoveryClient.getInstances(nomeMicrosservico);
		if (instancias == null || instancias.isEmpty()) {
			throw new RuntimeException("Não foi possível encontrar a instância do microsserviço " + nomeMicrosservico);
		}

		ServiceInstance instanciaMicrosservico = instancias.get(0);
		int umaPortaMicrosservico = instanciaMicrosservico.getPort();
		return umaPortaMicrosservico;
	}
}
