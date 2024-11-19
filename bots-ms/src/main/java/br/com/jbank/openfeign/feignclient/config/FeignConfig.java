package br.com.jbank.openfeign.feignclient.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbank.dto.FracaoRespostaModeloDto;
import feign.codec.Decoder;

@Configuration
public class FeignConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Decoder ndjsonDecoder() {
        return (response, type) -> {
            List<FracaoRespostaModeloDto> resultList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    resultList.add(objectMapper.readValue(line, FracaoRespostaModeloDto.class));
                }
            }
            return ResponseEntity.ok(resultList);
        };
    }
}
