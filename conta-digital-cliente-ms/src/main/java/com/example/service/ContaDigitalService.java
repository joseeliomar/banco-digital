package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.enumeration.TipoDocumento;
import com.example.exception.ValidacaoException;
import com.example.model.ContaDigitalCliente;
import com.example.model.ContaDigitalPessoaFisica;
import com.example.model.ContaDigitalPessoaJuridica;
import com.example.repository.ContaDigitalPessoaFisicaRepository;
import com.example.repository.ContaDigitalPessoaJuridicaRepository;

public abstract class ContaDigitalService {
	
	@Autowired
	protected ContaDigitalPessoaFisicaRepository contaDigitalPessoaFisicaRepository;
	
	@Autowired
	protected ContaDigitalPessoaJuridicaRepository contaDigitalPessoaJuridicaRepository;
	
	public abstract Optional<? extends ContaDigitalCliente> buscaContaDigitalPelaAgenciaConta(String agencia,
			String conta);

	protected void validaAgencia(String agencia) {
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
	
	protected void validaConta(String conta) {
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
	
	protected void validaAgenciaContaInsercao(String agencia, String conta) {
		var contaDigitalOptional = buscaContaDigitalPelaAgenciaConta(agencia, conta);
		if (contaDigitalOptional.isPresent()) {
			throw new ValidacaoException(
					"Já existe uma conta digital cadastrada a agência " + agencia + " e a conta " + conta + ".",
					HttpStatus.CONFLICT);
		}
	}
	
	
	protected void validaAgenciaContaAlteracao(String agencia, String conta, DocumentoCliente documentoCliente) {
		String numeroDocumento = documentoCliente.numeroDocumento();
		TipoDocumento tipoDocumento = documentoCliente.TipoDocumento();
		Optional<ContaDigitalPessoaFisica> contaDigitalPessoaFisicaOptional;
		Optional<ContaDigitalPessoaJuridica> contaDigitalPessoaJuridicaOptional;

		if (tipoDocumento == TipoDocumento.CPF) {
			contaDigitalPessoaFisicaOptional = contaDigitalPessoaFisicaRepository
					.findByAgenciaAndContaAndCpfNot(agencia, conta, numeroDocumento);

			contaDigitalPessoaJuridicaOptional = contaDigitalPessoaJuridicaRepository.findByAgenciaAndConta(agencia,
					conta);
		} else if (tipoDocumento == TipoDocumento.CNPJ) {
			contaDigitalPessoaFisicaOptional = contaDigitalPessoaFisicaRepository.findByAgenciaAndConta(agencia, conta);

			contaDigitalPessoaJuridicaOptional = contaDigitalPessoaJuridicaRepository
					.findByAgenciaAndContaAndCnpjNot(agencia, conta, numeroDocumento);
		} else {
			throw new ValidacaoException("O tipo de documento que foi informado está incorreto.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (contaDigitalPessoaFisicaOptional.isPresent() || contaDigitalPessoaJuridicaOptional.isPresent()) {
			throw new ValidacaoException(
					"Já existe uma outra conta digital cadastrada a agência " + agencia + " e a conta " + conta + ".",
					HttpStatus.CONFLICT);
		}
	}
	
	protected void validaSenha(String senha) {
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

	protected void validaTelefone(String telefone) {
		if (telefone == null || telefone.isBlank()) {
			throw new ValidacaoException("Telefone não informado.", HttpStatus.BAD_REQUEST);
		}
		if (telefone.length() > 11) {
			throw new ValidacaoException("Telefone com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
	}
	
	protected void validaEmail(String email) {
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
	
}
