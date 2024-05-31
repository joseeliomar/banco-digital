package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaFisicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaFisicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaFisicaInsercaoDto;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.repository.ContaDigitalPessoaFisicaRepository;

@Service
public class ContaDigitalPessoaFisicaService extends ContaDigitalService {
	
	@Autowired
	private ContaDigitalPessoaFisicaRepository repository;
	
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
		super.validaSenha(senha);
		super.validaTelefone(telefone);
		super.validaEmail(email);
		validaCpf(cpf);
		validaNomeCompleto(nomeCompleto);
		validaDataNascimento(dataNascimento);
		validaNomeCompletoMae(nomeCompletoMae);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = new ContaDigitalPessoaFisica(agencia, conta, senha, telefone, email, null, dataHoraCadastro, null, cpf, nomeCompleto, dataNascimento, nomeCompletoMae);
		return repository.save(contaDigitalPessoaFisica);
	}

	private void validaCpf(String cpf) {
		if (cpf == null || cpf.isBlank()) {
			throw new ValidacaoException("CPF não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() < 11) {
			throw new ValidacaoException("CPF com menos de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() > 11) {
			throw new ValidacaoException("CPF com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
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
		validaSenha(senha);
		validaTelefone(telefone);
		validaEmail(email);
		validaCpf(cpf);
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
		return repository.save(contaDigitalPessoaFisicaSalvaBancoDados);
	}

	public Optional<ContaDigitalPessoaFisica> buscaContaDigitalPeloCpf(String cpf) {
		return repository.findById(cpf);
	}
	
	public ContaDigitalPessoaFisicaDTO1Busca buscaContaDigitalPeloCpfComRespostaSemSenha(String cpf) {
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
		
		if (contaDigitalPessoaFisicaOptional.isPresent()) {
			return new ContaDigitalPessoaFisicaDTO1Busca(contaDigitalPessoaFisicaOptional.get());
		}
		
		return null;
	}

	public Optional<ContaDigitalPessoaFisica> buscaContaDigitalPelaAgenciaConta(String agencia, String conta) {
		return repository.findByAgenciaAndConta(agencia, conta);
	}

	public void removeContaDigitalPessoaFisica(String cpf) {
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigitalPeloCpf(cpf);
		
		ContaDigitalPessoaFisica contaDigitalPessoaFisicaSalvaBancoDados = contaDigitalPessoaFisicaOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o CPF informado.", HttpStatus.BAD_REQUEST));
		
		repository.delete(contaDigitalPessoaFisicaSalvaBancoDados);
	}
}
