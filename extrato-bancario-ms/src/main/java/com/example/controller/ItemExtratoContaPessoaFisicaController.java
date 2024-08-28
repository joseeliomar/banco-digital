package com.example.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ItemExtratoContaPessoaFisicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaFisicaInseridoDto;
import com.example.model.ItemExtratoContaPessoaFisica;
import com.example.service.ExtratoContaPessoaFisicaService;
import com.example.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itemExtratoContaPessoaFisica")
@Tag(name = "Item de extrato de conta corrente para pessoa física")
public class ItemExtratoContaPessoaFisicaController {

	@Autowired
	private ExtratoContaPessoaFisicaService extratoContaPessoaFisicaService;

	@Operation(summary = "Realização a inserção de um item de extrato")
	@PostMapping("/")
	public ResponseEntity<ItemExtratoContaPessoaFisicaInseridoDto> insereItemExtratoContaPessoaFisica(
			@RequestBody ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto) {
		ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica = extratoContaPessoaFisicaService
				.insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto);
		ItemExtratoContaPessoaFisicaInseridoDto itemExtratoContaPessoaFisicaInseridoDto = new ItemExtratoContaPessoaFisicaInseridoDto(
				itemExtratoContaPessoaFisica);
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(itemExtratoContaPessoaFisicaInseridoDto.getId());
		return ResponseEntity.created(uriRecursoCriado).body(itemExtratoContaPessoaFisicaInseridoDto);
	}
	
	@Operation(summary = "Realização a exclusão de um item de extrato")
	@DeleteMapping("/{idItemExtratoContaPessoaFisica}")
	public ResponseEntity<?> removeItemExtratoContaPessoaFisica(@PathVariable Long idItemExtratoContaPessoaFisica) {
		extratoContaPessoaFisicaService.removeItemExtratoContaPessoaFisica(idItemExtratoContaPessoaFisica);
		return ResponseEntity.noContent().build();
	}
}
