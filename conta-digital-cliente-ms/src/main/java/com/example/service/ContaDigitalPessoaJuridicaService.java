package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dto.ContaDigitalPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaDigitalPessoaJuridicaDTO1Busca;
import com.example.dto.ContaDigitalPessoaJuridicaInsercaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.DocumentoCliente;
import com.example.enumeration.TipoConta;
import com.example.enumeration.TipoDocumento;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.openfeign.feignclient.dto.DadosContaDto;

@Service
public class ContaDigitalPessoaJuridicaService extends ContaDigitalService {
	
	public ContaDigitalPessoaJuridica insereContaDigitalPessoaJuridica(
			ContaDigitalPessoaJuridicaInsercaoDto contaDigitalPessoaJuridicaInsercaoDto) {
		String agencia = contaDigitalPessoaJuridicaInsercaoDto.getAgencia();
		String senha = contaDigitalPessoaJuridicaInsercaoDto.getSenha();
		String telefone = contaDigitalPessoaJuridicaInsercaoDto.getTelefone();
		String email = contaDigitalPessoaJuridicaInsercaoDto.getEmail();
		String cnpj = contaDigitalPessoaJuridicaInsercaoDto.getCnpj();
		String razaoSocial = contaDigitalPessoaJuridicaInsercaoDto.getRazaoSocial();
		
		DadosContaDto dadosContaDto = super.geraDadosConta();
		String numeroConta = dadosContaDto.numeroConta();
		int digitoVerificadorConta = dadosContaDto.digitoVerificadorConta();
		
		super.validaAgencia(agencia);
		super.validaConta(numeroConta);
		super.validaDigitoVerificadorConta(digitoVerificadorConta);
		super.validaAgenciaContaInsercao(agencia, numeroConta);
		super.validaSenha(senha);
		super.validaTelefone(telefone);
		super.validaEmail(email);
		validaCnpj(cnpj, true);
		validaRazaoSocial(razaoSocial);
		
		LocalDateTime dataHoraCadastro = LocalDateTime.now();
		var senhaCriptografada = criptografaSenha(senha);
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridica = new ContaDigitalPessoaJuridica(agencia, numeroConta, senhaCriptografada,
				telefone, email, null, dataHoraCadastro, null, cnpj, razaoSocial, digitoVerificadorConta);
		var contaDigitalPessoaJuridicaCadastrada = contaDigitalPessoaJuridicaRepository.save(contaDigitalPessoaJuridica);
		
		var contaCorrentePessoaJuridica = new ContaPessoaJuridicaInsercaoDto(TipoConta.CORRENTE, cnpj);
		var contaPoupancaPessoaJuridica = new ContaPessoaJuridicaInsercaoDto(TipoConta.POUPANCA, cnpj);
		contaCorrentePoupancaMsFeignClient.insereContaPessoaJuridica(contaCorrentePessoaJuridica);
		contaCorrentePoupancaMsFeignClient.insereContaPessoaJuridica(contaPoupancaPessoaJuridica);
		
		return contaDigitalPessoaJuridicaCadastrada;
	}
	
	private void validaCnpj(String cnpj, boolean isInsercaoContaDigital) {
		if (cnpj == null || cnpj.isBlank()) {
			throw new ValidacaoException("CNPJ não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() < 14) {
			throw new ValidacaoException("CNPJ com menos de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() > 14) {
			throw new ValidacaoException("CNPJ com mais de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		
		if (isInsercaoContaDigital) {
			Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cnpj);
			if (contaDigitalPessoaJuridicaOptional.isPresent()) {
				throw new ValidacaoException("Já existe uma conta digital cadastrada com o CNPJ " + cnpj 
						+ ".", HttpStatus.CONFLICT);
			}
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
		String senha = contaDigitalPessoaJuridicaAlteracaoDto.getSenha();
		String telefone = contaDigitalPessoaJuridicaAlteracaoDto.getTelefone();
		String email = contaDigitalPessoaJuridicaAlteracaoDto.getEmail();
		Long idEndereco = contaDigitalPessoaJuridicaAlteracaoDto.getIdEndereco();
		String cnpj = contaDigitalPessoaJuridicaAlteracaoDto.getCnpj();
		String razaoSocial = contaDigitalPessoaJuridicaAlteracaoDto.getRazaoSocial();
		
		validaAgencia(agencia);
		validaCnpj(cnpj, false);
		validaSenha(senha);
		validaTelefone(telefone);
		validaEmail(email);
		validaRazaoSocial(razaoSocial);
		
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cnpj);
		
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridicaSalvaBancoDados = contaDigitalPessoaJuridicaOptional
				.orElseThrow(
						() -> new ValidacaoException("Não foi encontrada uma conta com o CNPJ informado.", HttpStatus.BAD_REQUEST));
		
		String conta = contaDigitalPessoaJuridicaSalvaBancoDados.getConta();
		validaAgenciaContaAlteracao(agencia, conta, new DocumentoCliente(cnpj, TipoDocumento.CNPJ));
		
		contaDigitalPessoaJuridicaSalvaBancoDados.setAgencia(agencia);
		contaDigitalPessoaJuridicaSalvaBancoDados.setSenha(senha);
		contaDigitalPessoaJuridicaSalvaBancoDados.setTelefone(telefone);
		contaDigitalPessoaJuridicaSalvaBancoDados.setEmail(email);
		contaDigitalPessoaJuridicaSalvaBancoDados.setIdEndereco(idEndereco);
		contaDigitalPessoaJuridicaSalvaBancoDados.setRazaoSocial(razaoSocial);
		
		contaDigitalPessoaJuridicaSalvaBancoDados.setDataHoraAlteracao(LocalDateTime.now());
		return contaDigitalPessoaJuridicaRepository.save(contaDigitalPessoaJuridicaSalvaBancoDados);
	}
	
	public Optional<ContaDigitalPessoaJuridica> buscaContaDigitalPeloCnpj(String cpnj) {
		return contaDigitalPessoaJuridicaRepository.findById(cpnj);
	}
	
	public ContaDigitalPessoaJuridicaDTO1Busca buscaContaDigitalPeloCnpjComRespostaSemSenha(String cpnj) {
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cpnj);
		
		if (contaDigitalPessoaJuridicaOptional.isPresent()) {
			return new ContaDigitalPessoaJuridicaDTO1Busca(contaDigitalPessoaJuridicaOptional.get());
		}
		
		return null;
	}

	@Override
	public Optional<ContaDigitalPessoaJuridica> buscaContaDigitalPelaAgenciaConta(String agencia, String conta) {
		return contaDigitalPessoaJuridicaRepository.findByAgenciaAndConta(agencia, conta);
	}
	
	public void removeContaDigitalPessoaJuridica(String cpnj) {
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaDigitalPeloCnpj(cpnj);
		
		ContaDigitalPessoaJuridica contaDigitalPessoaJuridicaSalvaBancoDados = contaDigitalPessoaJuridicaOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o CNPJ informado.", HttpStatus.BAD_REQUEST));
		
		removeContaPessoaJuridica(cpnj, TipoConta.CORRENTE);
		removeContaPessoaJuridica(cpnj, TipoConta.POUPANCA);
		
		contaDigitalPessoaJuridicaRepository.delete(contaDigitalPessoaJuridicaSalvaBancoDados);
	}

	private void removeContaPessoaJuridica(String cpnj, TipoConta tipoConta) {
		ResponseEntity<ContaPessoaJuridicaBuscaDto1> respostaBuscaContaPessoaJuridica = contaCorrentePoupancaMsFeignClient
				.buscaContaPessoaJuridica(cpnj, tipoConta);
		
		var contaPessoaJuridica = respostaBuscaContaPessoaJuridica.getBody();
		
		if (contaPessoaJuridica != null) {
			contaCorrentePoupancaMsFeignClient.removeContaPessoaJuridica(contaPessoaJuridica.getId());
		}
	}
	
}
