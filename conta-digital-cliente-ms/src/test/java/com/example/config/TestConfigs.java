package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestConfigs {
	
	@Autowired
	private Environment environment;

	public int getServerPort() {
		return Integer.parseInt(environment.getProperty("local.server.port"));
	}
}