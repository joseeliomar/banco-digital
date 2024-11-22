package br.com.jbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BotsMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotsMsApplication.class, args);
	}

}
