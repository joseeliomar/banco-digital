package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.enumeration.DocumentoCliente;
import com.example.enumeration.TipoConta;
import com.example.enumeration.TipoDocumento;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.openfeign.feignclient.ContaCorrentePoupancaMsFeignClient;

@Service
public class ContaDigitalPessoaFisicaService extends ContaDigitalService {
	
	@Autowired
	private ContaCorrentePoupancaMsFeignClient contaCorrentePoupancaMsFeignClient;
	
	public ContaDigitalPessoaFisica insereContaDigitalPessoaFisica(ContaDigitalPessoaFisicaInsercaoDto contaDigitalPessoaFisicaInsercaoDto) {
		String agencia = contaDigitalPessoaFisicaInsercaoDto.getAgencia();
		String conta = contaDigitalPessoaFisicaInsercaoDto.getConta();
		String senha = contaDigitalPessoaFisicaInsercaoDto.getSenha();
		String telefone = contaDigitalPessoaFisicaInsercaoDto.getTelefone();
		String email = contaDigitalPessoaFisicaInsercaoDto.getEmail();
		String cpf = contaDigitalPessoaFisicaInsercaoDto.getCpf();
		String nomeCompleto = contaDigitalPessoaFisicaInsercaoDto.getNomeCompleto();
		LocalDate dataNascimento = contaDigitalPessoaFisicaInsercaoDto.getDataNascimento();
		String nomeCompletoMae = contaDigitalPessoaFisicaInsercaoDto.getNomeCompletoMae();
		
		super.validaAgencia(agencia);
		super.validaConta(conta);
		super.validaAgenciaContaInsercao(agencia, conta);
		super.validaSenha(senha);
		super.validaTelefone(telefone);
		super.validaEmail(email);
		validaCpf(cpf, true);
		validaNomeCompleto(nomeCompleto);
		validaDataNascimento(dataNascimento);
		validaNomeCompletoMae(nomeCompletoMae);
		
		// criar atributo para o digito verificador da conta
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = new ContaDigitalPessoaFisica(agencia, conta, senha,
				telefone, email, null, dataHoraCadastro, null, cpf, nomeCompleto, dataNascimento, nomeCompletoMae);
		var contaDigitalPessoaFisicaCadastrada = contaDigitalPessoaFisicaRepository.save(contaDigitalPessoaFisica);
		
		var contaCorrentePessoaFisica = new ContaPessoaFisicaInsercaoDto(TipoConta.CORRENTE, cpf);
		var contaPoupancaPessoaFisica = new ContaPessoaFisicaInsercaoDto(TipoConta.POUPANCA, cpf);
		contaCorrentePoupancaMsFeignClient.insereContaPessoaFisica(contaCorrentePessoaFisica);
		contaCorrentePoupancaMsFeignClient.insereContaPessoaFisica(contaPoupancaPessoaFisica);
		
		return contaDigitalPessoaFisicaCadastrada;
	}

	private void validaCpf(String cpf, boolean isInsercaoContaDigital) {
		if (cpf == null || cpf.isBlank()) {
			throw new ValidacaoException("CPF não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() < 11) {
			throw new ValidacaoException("CPF com menos de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() > 11) {
			throw new ValidacaoException("CPF com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		
		if (isInsercaoContaDigital) {
			Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
			if (contaDigitalPessoaFisicaOptional.isPresent()) {
				throw new ValidacaoException("Já existe uma conta digital cadastrada com o CPF " + cpf 
						+ ".", HttpStatus.CONFLICT);
			}
		}
	}
	
	private void validaNomeCompleto(String nomeCompleto) {
		if (nomeCompleto == null || nomeCompleto.isBlank()) {
			throw new ValidacaoException("Nome completo não informado.", HttpStatus.BAD_REQUEST);
		}
		if (nomeCompleto.length() > 100) {
			throw new ValidacaoException("Nome completo com mais de 100 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaDataNascimento(LocalDate dataNascimento) {
		if (dataNascimento == null) {
			throw new ValidacaoException("Data nascimento não informada.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaNomeCompletoMae(String nomeCompletoMae) {
		if (nomeCompletoMae == null || nomeCompletoMae.isBlank()) {
			throw new ValidacaoException("Nome completo da mãe não informado.", HttpStatus.BAD_REQUEST);
		}
		if (nomeCompletoMae.length() > 100) {
			throw new ValidacaoException("Nome completo da mãe com mais de 100 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public ContaDigitalPessoaFisica alteraContaDigitalPessoaFisica(ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisicaAlteracaoDto) {
		String agencia = contaDigitalPessoaFisicaAlteracaoDto.getAgencia();
		String conta = contaDigitalPessoaFisicaAlteracaoDto.getConta();
		String senha = contaDigitalPessoaFisicaAlteracaoDto.getSenha();
		String telefone = contaDigitalPessoaFisicaAlteracaoDto.getTelefone();
		String email = contaDigitalPessoaFisicaAlteracaoDto.getEmail();
		Long idEndereco = contaDigitalPessoaFisicaAlteracaoDto.getIdEndereco();
		String cpf = contaDigitalPessoaFisicaAlteracaoDto.getCpf();
		String nomeCompleto = contaDigitalPessoaFisicaAlteracaoDto.getNomeCompleto();
		LocalDate dataNascimento = contaDigitalPessoaFisicaAlteracaoDto.getDataNascimento();
		String nomeCompletoMae = contaDigitalPessoaFisicaAlteracaoDto.getNomeCompletoMae();
		
		validaAgencia(agencia);
		validaConta(conta);
		validaCpf(cpf, false);
		validaAgenciaContaAlteracao(agencia, conta, new DocumentoCliente(cpf, TipoDocumento.CPF));
		validaSenha(senha);
		validaTelefone(telefone);
		validaEmail(email);
		validaNomeCompleto(nomeCompleto);
		validaDataNascimento(dataNascimento);
		validaNomeCompletoMae(nomeCompletoMae);
		
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
		
		ContaDigitalPessoaFisica contaDigitalPessoaFisicaSalvaBancoDados = contaDigitalPessoaFisicaOptional
				.orElseThrow(
						() -> new ValidacaoException("Não foi encontrada uma conta com o CPF informado.", HttpStatus.BAD_REQUEST));
		
		contaDigitalPessoaFisicaSalvaBancoDados.setAgencia(agencia);
		contaDigitalPessoaFisicaSalvaBancoDados.setConta(conta);
		contaDigitalPessoaFisicaSalvaBancoDados.setSenha(senha);
		contaDigitalPessoaFisicaSalvaBancoDados.setTelefone(telefone);
		contaDigitalPessoaFisicaSalvaBancoDados.setEmail(email);
		contaDigitalPessoaFisicaSalvaBancoDados.setIdEndereco(idEndereco);
		contaDigitalPessoaFisicaSalvaBancoDados.setNomeCompleto(nomeCompleto);
		contaDigitalPessoaFisicaSalvaBancoDados.setDataNascimento(dataNascimento);
		contaDigitalPessoaFisicaSalvaBancoDados.setNomeCompletoMae(nomeCompletoMae);
		
		contaDigitalPessoaFisicaSalvaBancoDados.setDataHoraAlteracao(LocalDateTime.now());
		return contaDigitalPessoaFisicaRepository.save(contaDigitalPessoaFisicaSalvaBancoDados);
	}

	public Optional<ContaDigitalPessoaFisica> buscaContaDigitalPeloCpf(String cpf) {
		return contaDigitalPessoaFisicaRepository.findById(cpf);
	}
	
	public ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalPeloCpfComRespostaSemSenha(String cpf) {
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
		
		if (contaDigitalPessoaFisicaOptional.isPresent()) {
			return new ContaDigitalPessoaFisicaDTO1Busca(contaDigitalPessoaFisicaOptional.get());
		}
		
		return null;
	}

	@Override
	public Optional<ContaDigitalPessoaFisica> buscaContaDigitalPelaAgenciaConta(String agencia, String conta) {
		return contaDigitalPessoaFisicaRepository.findByAgenciaAndConta(agencia, conta);
	}
	
	public void removeContaDigitalPessoaFisica(String cpf) {
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
		
		ContaDigitalPessoaFisica contaDigitalPessoaFisicaSalvaBancoDados = contaDigitalPessoaFisicaOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o CPF informado.", HttpStatus.BAD_REQUEST));
		
		removeContaPessoaFisica(cpf, TipoConta.CORRENTE);
		removeContaPessoaFisica(cpf, TipoConta.POUPANCA);
		
		contaDigitalPessoaFisicaRepository.delete(contaDigitalPessoaFisicaSalvaBancoDados);
	}
	
	private void removeContaPessoaFisica(String cpf, TipoConta tipoConta) {
		ResponseEntity<ContaPessoaFisicaBuscaDto1> respostaBuscaContaPessoaFisica = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaFisica(cpf, tipoConta);
		
		var contaPessoaFisica = respostaBuscaContaPessoaFisica.getBody();
		
		if (contaPessoaFisica != null) {
			contaCorrentePoupancaMsFeignClient.removeContaPessoaFisica(contaPessoaFisica.getId());
		}
	}
}
