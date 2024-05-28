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
public class ContaDigitalPessoaFisicaService {
	
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
		
		validaAgencia(agencia);
		validaConta(conta);
		validaSenha(senha);
		validaTelefone(telefone);
		validaEmail(email);
		validaCpf(cpf);
		validaNomeCompleto(nomeCompleto);
		validaDataNascimento(dataNascimento);
		validaNomeCompletoMae(nomeCompletoMae);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ContaDigitalPessoaFisica contaDigitalPessoaFisica = new ContaDigitalPessoaFisica(agencia, conta, senha, telefone, email, null, dataHoraCadastro, null, cpf, nomeCompleto, dataNascimento, nomeCompletoMae);
		return repository.save(contaDigitalPessoaFisica);
	}

	private void validaAgencia(String agencia) {
		if (agencia == null || agencia.isBlank()) {
			throw new ValidacaoException("Agência não informada.", HttpStatus.BAD_REQUEST);
		}
		if (agencia.length() < 10) {
			throw new ValidacaoException("Agência com menos de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (agencia.length() > 10) {
			throw new ValidacaoException("Agência com mais de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaConta(String conta) {
		if (conta == null || conta.isBlank()) {
			throw new ValidacaoException("Conta não informada.", HttpStatus.BAD_REQUEST);
		}
		if (conta.length() < 10) {
			throw new ValidacaoException("Conta com menos de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (conta.length() > 10) {
			throw new ValidacaoException("Conta com mais de 10 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaSenha(String senha) {
		if (senha == null || senha.isBlank()) {
			throw new ValidacaoException("Senha não informada.", HttpStatus.BAD_REQUEST);
		}
		if (senha.length() < 8) {
			throw new ValidacaoException("Senha com menos de 8 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (senha.length() > 16) {
			throw new ValidacaoException("Senha com mais de 16 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}

	private void validaTelefone(String telefone) {
		if (telefone == null || telefone.isBlank()) {
			throw new ValidacaoException("Telefone não informado.", HttpStatus.BAD_REQUEST);
		}
		if (telefone.length() > 11) {
			throw new ValidacaoException("Telefone com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private void validaEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new ValidacaoException("E-mail não informado.", HttpStatus.BAD_REQUEST);
		}
		if (!email.contains("@")) {
			throw new ValidacaoException("E-mail informado sem o símbolo @ (arroba).", HttpStatus.BAD_REQUEST);
		}
		if (email.length() > 50) {
			throw new ValidacaoException("E-mail com mais de 50 caracteres.", HttpStatus.BAD_REQUEST);
		}
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
	
	public ContaDigitalPessoaFisica alteraContaDigitalPessoaFisica(ContaDigitalPessoaFisicaAlteracaoDto contaDigitalPessoaFisica) {
		String agencia = contaDigitalPessoaFisica.getAgencia();
		String conta = contaDigitalPessoaFisica.getConta();
		String senha = contaDigitalPessoaFisica.getSenha();
		String telefone = contaDigitalPessoaFisica.getTelefone();
		String email = contaDigitalPessoaFisica.getEmail();
		Long idEndereco = contaDigitalPessoaFisica.getIdEndereco();
		String cpf = contaDigitalPessoaFisica.getCpf();
		String nomeCompleto = contaDigitalPessoaFisica.getNomeCompleto();
		LocalDate dataNascimento = contaDigitalPessoaFisica.getDataNascimento();
		String nomeCompletoMae = contaDigitalPessoaFisica.getNomeCompletoMae();
		
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
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional = repository.findById(cpf);
		
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
