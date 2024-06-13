package com.example.controller.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dto.DetalhesExcecaoDto;
import com.example.exception.ExcecaoComStatusHttp;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManiputadorExcecoesController {

	@ExceptionHandler(value = { ExcecaoComStatusHttp.class })
	public ResponseEntity<DetalhesExcecaoDto> respostaTratada(ExcecaoComStatusHttp excecaoLancada,
			HttpServletRequest httpServletRequest) {
		HttpStatus httpStatus = excecaoLancada.getHttpStatus();

		DetalhesExcecaoDto detalhesExcecaoDto = new DetalhesExcecaoDto(LocalDateTime.now(), httpStatus.value(),
				excecaoLancada.getMessage(), httpServletRequest.getRequestURI());

		return ResponseEntity.status(httpStatus).body(detalhesExcecaoDto);
	}
}