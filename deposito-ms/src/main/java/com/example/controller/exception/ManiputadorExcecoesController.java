package com.example.controller.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dto.DetalhesExcecaoDto;
import com.example.exception.ExcecaoComStatusHttp;
import com.example.openfeign.exception.OpenFeignStatusHttpException;

import feign.FeignException.FeignClientException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManiputadorExcecoesController {

	@ExceptionHandler(value = { FeignClientException.class })
	public ResponseEntity<DetalhesExcecaoDto> respostaTratada(FeignClientException excecaoLancada,
			HttpServletRequest httpServletRequest) {
		int httpStatus = excecaoLancada.status();

		DetalhesExcecaoDto detalhesExcecaoDto = new DetalhesExcecaoDto(LocalDateTime.now(), httpStatus,
				excecaoLancada.getMessage(), httpServletRequest.getRequestURI());

		return ResponseEntity.status(httpStatus).body(detalhesExcecaoDto);
	}
	
	@ExceptionHandler(value = { ExcecaoComStatusHttp.class })
	public ResponseEntity<DetalhesExcecaoDto> respostaTratada(OpenFeignStatusHttpException excecaoLancada,
			HttpServletRequest httpServletRequest) {
		HttpStatus httpStatus = excecaoLancada.getHttpStatus();

		DetalhesExcecaoDto detalhesExcecaoDto = new DetalhesExcecaoDto(LocalDateTime.now(), httpStatus.value(),
				excecaoLancada.getMessage(), httpServletRequest.getRequestURI());

		return ResponseEntity.status(httpStatus).body(detalhesExcecaoDto);
	}
}