package com.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.model.ContaDigitalPessoaFisica;
import com.example.service.exceptions.ValidacaoException;

@Service
public class ContaDigitalPessoaFisicaService {
	
	public ContaDigitalPessoaFisica criaContaDigitalPessoaFisica(ContaDigitalPessoaFisica contaDigitalPessoaFisica) {
		String agencia = contaDigitalPessoaFisica.getAgencia();
		String conta = contaDigitalPessoaFisica.getConta();
		String senha = contaDigitalPessoaFisica.getSenha();
		String telefone = contaDigitalPessoaFisica.getTelefone();
		
		validaAgencia(agencia);
		validaConta(conta);
		validaSenha(senha);
		validaTelefone(telefone);
		
		return new ContaDigitalPessoaFisica();
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
		if (senha.length() > 10) {
			throw new ValidacaoException("Senha com mais de 10 caracteres.", HttpStatus.BAD_REQUEST);
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
}
