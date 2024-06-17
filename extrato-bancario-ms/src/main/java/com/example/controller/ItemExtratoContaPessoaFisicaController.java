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
import com.example.service.ItemExtratoContaPessoaFisicaService;
import com.example.utils.Utils;

@RestController
@RequestMapping("/itemExtratoContaPessoaFisica")
public class ItemExtratoContaPessoaFisicaController {

	@Autowired
	private ItemExtratoContaPessoaFisicaService itemExtratoContaPessoaFisicaService;

	@PostMapping("/")
	public ResponseEntity<ItemExtratoContaPessoaFisicaInseridoDto> insereItemExtratoContaPessoaFisica(
			@RequestBody ItemExtratoContaPessoaFisicaInsercaoDto itemExtratoContaPessoaFisicaInsercaoDto) {
		ItemExtratoContaPessoaFisica itemExtratoContaPessoaFisica = itemExtratoContaPessoaFisicaService
				.insereItemExtratoContaPessoaFisica(itemExtratoContaPessoaFisicaInsercaoDto);
		ItemExtratoContaPessoaFisicaInseridoDto itemExtratoContaPessoaFisicaInseridoDto = new ItemExtratoContaPessoaFisicaInseridoDto(
				itemExtratoContaPessoaFisica);
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(itemExtratoContaPessoaFisicaInseridoDto.getId());
		return ResponseEntity.created(uriRecursoCriado).body(itemExtratoContaPessoaFisicaInseridoDto);
	}

	@DeleteMapping("/{idItemExtratoContaPessoaFisica}")
	public ResponseEntity<?> removeItemExtratoContaPessoaFisica(@PathVariable Long idItemExtratoContaPessoaFisica) {
		itemExtratoContaPessoaFisicaService.removeItemExtratoContaPessoaFisica(idItemExtratoContaPessoaFisica);
		return ResponseEntity.noContent().build();
	}
}
