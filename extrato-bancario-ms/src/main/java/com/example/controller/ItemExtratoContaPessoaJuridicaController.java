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

import com.example.dto.ItemExtratoContaPessoaJuridicaInsercaoDto;
import com.example.dto.ItemExtratoContaPessoaJuridicaInseridoDto;
import com.example.model.ItemExtratoContaPessoaJuridica;
import com.example.service.ExtratoContaPessoaJuridicaService;
import com.example.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itemExtratoContaPessoaJuridica")
@Tag(name = "Item de extrato de conta corrente para pessoa jurídica")
public class ItemExtratoContaPessoaJuridicaController {

	@Autowired
	private ExtratoContaPessoaJuridicaService extratoContaPessoaJuridicaService;

	@Operation(summary = "Realiza a inserção de um item de extrato")
	@PostMapping("/")
	public ResponseEntity<ItemExtratoContaPessoaJuridicaInseridoDto> insereItemExtratoContaPessoaJuridica(
			@RequestBody ItemExtratoContaPessoaJuridicaInsercaoDto itemExtratoContaPessoaJuridicaInsercaoDto) {
		ItemExtratoContaPessoaJuridica itemExtratoContaPessoaJuridica = extratoContaPessoaJuridicaService
				.insereItemExtratoContaPessoaJuridica(itemExtratoContaPessoaJuridicaInsercaoDto);
		ItemExtratoContaPessoaJuridicaInseridoDto itemExtratoContaPessoaJuridicaInseridoDto = new ItemExtratoContaPessoaJuridicaInseridoDto(
				itemExtratoContaPessoaJuridica);
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(itemExtratoContaPessoaJuridicaInseridoDto.getId());
		return ResponseEntity.created(uriRecursoCriado).body(itemExtratoContaPessoaJuridicaInseridoDto);
	}

	@Operation(summary = "Realiza a exclusão de um item de extrato")
	@DeleteMapping("/{idItemExtratoContaPessoaJuridica}")
	public ResponseEntity<?> removeItemExtratoContaPessoaJuridica(@PathVariable Long idItemExtratoContaPessoaJuridica) {
		extratoContaPessoaJuridicaService.removeItemExtratoContaPessoaJuridica(idItemExtratoContaPessoaJuridica);
		return ResponseEntity.noContent().build();
	}
}
