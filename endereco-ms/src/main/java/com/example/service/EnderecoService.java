package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.EnderecoAlteracaoDto;
import com.example.dto.EnderecoBuscaDto1;
import com.example.dto.EnderecoInsercaoDto;
import com.example.enumeration.UnidadeFederativa;
import com.example.exception.ValidacaoException;
import com.example.model.Endereco;
import com.example.repository.EnderecoRepository;

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Endereco insereEndereco(EnderecoInsercaoDto enderecoInsercaoDto1) {
		String rua = enderecoInsercaoDto1.getRua();
		Integer numero = enderecoInsercaoDto1.getNumero();
		String bairro = enderecoInsercaoDto1.getBairro();
		String municipio = enderecoInsercaoDto1.getMunicipio();
		UnidadeFederativa unidadeFederativa = enderecoInsercaoDto1.getUnidadeFederativa();
		String cep = enderecoInsercaoDto1.getCep();
		
		validaRua(rua);
		validaNumero(numero);
		validaBairro(bairro);
		validaMunicipio(municipio);
		validaUnidadeFederativa(unidadeFederativa);
		validaCep(cep);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		Endereco endereco = new Endereco(rua, numero, bairro, municipio, unidadeFederativa, cep, dataHoraCadastro, null);
		return enderecoRepository.save(endereco);
	}

	private void validaRua(String rua) {
		if (rua == null || rua.isBlank()) {
			throw new ValidacaoException("Rua não informada.", HttpStatus.BAD_REQUEST);
		}
		if (rua.length() > 80) {
			throw new ValidacaoException("Rua com mais de 80 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaNumero(Integer numero) {
		if (numero == null) {
			throw new ValidacaoException("Número não informado.", HttpStatus.BAD_REQUEST);
		}
		if (String.valueOf(numero.intValue()).length() > 4) {
			throw new ValidacaoException("Número com mais de 4 dígitos.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaBairro(String bairro) {
		if (bairro == null || bairro.isBlank()) {
			throw new ValidacaoException("Bairro não informado.", HttpStatus.BAD_REQUEST);
		}
		if (bairro.length() > 40) {
			throw new ValidacaoException("Bairro com mais de 40 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaMunicipio(String municipio) {
		if (municipio == null || municipio.isBlank()) {
			throw new ValidacaoException("Município não informado.", HttpStatus.BAD_REQUEST);
		}
		if (municipio.length() > 40) {
			throw new ValidacaoException("Município com mais de 40 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		if (unidadeFederativa == null) {
			throw new ValidacaoException("Unidade federativa não informada.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaCep(String cep) {
		if (cep == null || cep.isBlank()) {
			throw new ValidacaoException("CEP não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cep.length() > 8) {
			throw new ValidacaoException("CEP com mais de 8 dígitos.", HttpStatus.BAD_REQUEST);
		}
		if (cep.length() < 8) {
			throw new ValidacaoException("CEP com menos de 8 dígitos.", HttpStatus.BAD_REQUEST);
		}
	}

	public Endereco alteraEndereco(EnderecoAlteracaoDto enderecoAlteracaoDto1) {
		Long idEndereco = enderecoAlteracaoDto1.getId();
		String rua = enderecoAlteracaoDto1.getRua();
		Integer numero = enderecoAlteracaoDto1.getNumero();
		String bairro = enderecoAlteracaoDto1.getBairro();
		String municipio = enderecoAlteracaoDto1.getMunicipio();
		UnidadeFederativa unidadeFederativa = enderecoAlteracaoDto1.getUnidadeFederativa();
		String cep = enderecoAlteracaoDto1.getCep();
		
		validaRua(rua);
		validaNumero(numero);
		validaBairro(bairro);
		validaMunicipio(municipio);
		validaUnidadeFederativa(unidadeFederativa);
		validaCep(cep);
		
		Optional<Endereco> enderecoOptional = buscaEnderecoPeloId(idEndereco);
		
		Endereco enderecoSalvoBancoDados = enderecoOptional
				.orElseThrow(
						() -> new ValidacaoException("Não foi encontrado um endereço com o código informado.", HttpStatus.BAD_REQUEST));
		
		enderecoSalvoBancoDados.setId(idEndereco);
		enderecoSalvoBancoDados.setRua(rua);
		enderecoSalvoBancoDados.setNumero(numero);
		enderecoSalvoBancoDados.setBairro(bairro);
		enderecoSalvoBancoDados.setMunicipio(municipio);
		enderecoSalvoBancoDados.setUnidadeFederativa(unidadeFederativa);
		enderecoSalvoBancoDados.setCep(cep);
		
		enderecoSalvoBancoDados.setDataHoraAlteracao(LocalDateTime.now());	
		return enderecoRepository.save(enderecoSalvoBancoDados);
	}

	private Optional<Endereco> buscaEnderecoPeloId(Long id) {
		return enderecoRepository.findById(id);
	}

	public void removeEndereco(Long idEndereco) {
		Optional<Endereco> enderecoOptional = buscaEnderecoPeloId(idEndereco);
		
		Endereco enderecoSalvaBancoDados = enderecoOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrado um endereço com o código informado.", HttpStatus.BAD_REQUEST));
		
		enderecoRepository.delete(enderecoSalvaBancoDados);
	}

	public EnderecoBuscaDto1 buscaEnderecoCompleto(Long idEndereco) {
		Optional<Endereco> enderecoOptional = buscaEnderecoPeloId(idEndereco);
		
		if (enderecoOptional.isPresent()) {
			return new EnderecoBuscaDto1(enderecoOptional.get());
		}
		
		return null;
	}

	public Page<Endereco> buscaEnderecos(Pageable pageable) {
		return enderecoRepository.findAll(pageable);
	}
}
