package com.example.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.EnderecoAlteracaoDto;
import com.example.dto.EnderecoAlteradoDto;
import com.example.dto.EnderecoBuscaDto1;
import com.example.dto.EnderecoInsercaoDto;
import com.example.dto.EnderecoInseridoDto;
import com.example.model.Endereco;
import com.example.service.EnderecoService;
import com.example.utils.Utils;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;

	@PostMapping("/")
	public ResponseEntity<EnderecoInseridoDto> insereEndereco(@RequestBody EnderecoInsercaoDto enderecoInsercaoDto) {
		Endereco endereco = enderecoService.insereEndereco(enderecoInsercaoDto);
		EnderecoInseridoDto enderecoInseridoDto = new EnderecoInseridoDto(endereco);
		URI uriRecursoCriado = Utils.obtemUriRecursoCriado(enderecoInseridoDto.getId());
		return ResponseEntity.created(uriRecursoCriado).body(enderecoInseridoDto);
	}
	
	@GetMapping("/{idEndereco}")
	public ResponseEntity<?> buscaEndereco(@PathVariable Long idEndereco) {
		EnderecoBuscaDto1 enderecoBuscaDTO1 = enderecoService.buscaEnderecoCompleto(idEndereco);
		
		if (enderecoBuscaDTO1 != null) {
			return ResponseEntity.ok(enderecoBuscaDTO1);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<EnderecoBuscaDto1>> buscaTarefas(Pageable pageable) {
		Page<Endereco> enderecosEncontrados = enderecoService.buscaEnderecos(pageable);
		
		if (enderecosEncontrados.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Page<EnderecoBuscaDto1> enderecosEncontradosDto = enderecosEncontrados.map(e -> new EnderecoBuscaDto1(e));

		return ResponseEntity.ok(enderecosEncontradosDto);
	}
	
	@PutMapping("/")
	public ResponseEntity<EnderecoAlteradoDto> alteraEndereco(@RequestBody EnderecoAlteracaoDto enderecoAlteracaoDto) {
		Endereco endereco = enderecoService.alteraEndereco(enderecoAlteracaoDto);
		return ResponseEntity.ok(new EnderecoAlteradoDto(endereco));
	}
	
	@DeleteMapping("/{idEndereco}")
	public ResponseEntity<?> removeEndereco(@PathVariable Long idEndereco) {
		enderecoService.removeEndereco(idEndereco);
		return ResponseEntity.noContent().build();
	}
}
