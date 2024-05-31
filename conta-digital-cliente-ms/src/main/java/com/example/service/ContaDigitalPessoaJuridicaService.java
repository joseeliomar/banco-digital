package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.repository.ContaDigitalPessoaJuridicaRepository;

@Service
public class ContaDigitalPessoaJuridicaService extends ContaDigitalService {
	
	@Autowired
	private ContaDigitalPessoaJuridicaRepository repository;

	public ContaDigitalPessoaJuridica insereContaDigitalPessoaJuridica(ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto) {
		String agencia = contaDigitalPessoaJuridicaInsercaoDto.getAgencia();
		String conta = contaDigitalPessoaJuridicaInsercaoDto.getConta();
		String senha = contaDigitalPessoaJuridicaInsercaoDto.getSenha();
		String telefone = contaDigitalPessoaJuridicaInsercaoDto.getTelefone();
		String email = contaDigitalPessoaJuridicaInsercaoDto.getEmail();
		String cnpj = contaDigitalPessoaJuridicaInsercaoDto.getCnpj();
		String razaoSocial = contaDigitalPessoaJuridicaInsercaoDto.getRazaoSocial();
		
		super.validaAgencia(agencia);
		super.validaConta(conta);
		super.validaSenha(senha);
		super.validaTelefone(telefone);
		super.validaEmail(email);
		validaCnpj(cnpj);
		validaRazaoSocial(razaoSocial);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridica = new ContaDigitalPessoaJuridica(agencia, conta, senha, telefone, email, null, dataHoraCadastro, null, cnpj, razaoSocial);
		return repository.save(contaDigitalPessoaJuridica);
	}
	
	private void validaCnpj(String cnpj) {
		if (cnpj == null || cnpj.isBlank()) {
			throw new ValidacaoException("CNPJ não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() < 14) {
			throw new ValidacaoException("CNPJ com menos de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() > 14) {
			throw new ValidacaoException("CNPJ com mais de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}

	private void validaRazaoSocial(String razaoSocial) {
		if (razaoSocial == null || razaoSocial.isBlank()) {
			throw new ValidacaoException("Razão social não informada.", HttpStatus.BAD_REQUEST);
		}
		if (razaoSocial.length() > 144) {
			throw new ValidacaoException("Razão social com mais de 144 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public ContaDigitalPessoaJuridica alteraContaDigitalPessoaJuridica(
			ContaDigitalPessoaJuridicaAlteracaoDto contaDigitalPessoaJuridicaAlteracaoDto) {
		String agencia = contaDigitalPessoaJuridicaAlteracaoDto.getAgencia();
		String conta = contaDigitalPessoaJuridicaAlteracaoDto.getConta();
		String senha = contaDigitalPessoaJuridicaAlteracaoDto.getSenha();
		String telefone = contaDigitalPessoaJuridicaAlteracaoDto.getTelefone();
		String email = contaDigitalPessoaJuridicaAlteracaoDto.getEmail();
		Long idEndereco = contaDigitalPessoaJuridicaAlteracaoDto.getIdEndereco();
		String cnpj = contaDigitalPessoaJuridicaAlteracaoDto.getCnpj();
		String razaoSocial = contaDigitalPessoaJuridicaAlteracaoDto.getRazaoSocial();
		
		validaAgencia(agencia);
		validaConta(conta);
		validaSenha(senha);
		validaTelefone(telefone);
		validaEmail(email);
		validaCnpj(cnpj);
		validaRazaoSocial(razaoSocial);
		
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cnpj);
		
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridicaSalvaBancoDados = contaDigitalPessoaJuridicaOptional
				.orElseThrow(
						() -> new ValidacaoException("Não foi encontrada uma conta com o CNPJ informado.", HttpStatus.BAD_REQUEST));
		
		contaDigitalPessoaJuridicaSalvaBancoDados.setAgencia(agencia);
		contaDigitalPessoaJuridicaSalvaBancoDados.setConta(conta);
		contaDigitalPessoaJuridicaSalvaBancoDados.setSenha(senha);
		contaDigitalPessoaJuridicaSalvaBancoDados.setTelefone(telefone);
		contaDigitalPessoaJuridicaSalvaBancoDados.setEmail(email);
		contaDigitalPessoaJuridicaSalvaBancoDados.setIdEndereco(idEndereco);
		contaDigitalPessoaJuridicaSalvaBancoDados.setRazaoSocial(razaoSocial);
		
		contaDigitalPessoaJuridicaSalvaBancoDados.setDataHoraAlteracao(LocalDateTime.now());
		return repository.save(contaDigitalPessoaJuridicaSalvaBancoDados);
	}
	
	public Optional<ContaDigitalPessoaJuridica> buscaContaDigitalPeloCnpj(String cpnj) {
		return repository.findById(cpnj);
	}
	
	public ContaDigitalPessoaJuridicaDTO1Busca buscaContaDigitalPeloCnpjComRespostaSemSenha(String cpnj) {
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cpnj);
		
		if (contaDigitalPessoaJuridicaOptional.isPresent()) {
			return new ContaDigitalPessoaJuridicaDTO1Busca(contaDigitalPessoaJuridicaOptional.get());
		}
		
		return null;
	}

	public Optional<ContaDigitalPessoaJuridica> buscaContaDigitalPelaAgenciaConta(String agencia, String conta) {
		return repository.findByAgenciaAndConta(agencia, conta);
	}

	public void removeContaDigitalPessoaJuridica(String cpnj) {
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cpnj);
		
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridicaSalvaBancoDados = contaDigitalPessoaJuridicaOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o CNPJ informado.", HttpStatus.BAD_REQUEST));
		
		repository.delete(contaDigitalPessoaJuridicaSalvaBancoDados);
	}
}
