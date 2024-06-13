package com.example;

import org.springframework.boot.SpringApplication;

public class TestContaCorrentePoupancaMsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContaCorrentePoupancaMsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
