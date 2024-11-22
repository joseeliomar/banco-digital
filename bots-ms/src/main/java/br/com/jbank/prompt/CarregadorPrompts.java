package br.com.jbank.prompt;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CarregadorPrompts {

	private final Map<String, String> prompts = new HashMap<>();

    @PostConstruct
    public void loadPrompts() throws IOException {
        Path promptDirectory = Paths.get("src/main/resources/prompts/");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(promptDirectory, "*.txt")) {
        	for (Path path : stream) {
        		String conteudo = Files.readString(path);
        		prompts.put(path.getFileName().toString().replace(".txt", ""), conteudo);
        	}
        	System.out.println("Prompts carregados: " + prompts.keySet());
        }
    }

    public String getPrompt(String fileName) {
        return prompts.get(fileName);
    }
}
