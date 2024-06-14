package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.ContaPessoaFisicaAlteracaoDto;
import com.example.dto.ContaPessoaFisicaBuscaDto1;
import com.example.dto.ContaPessoaFisicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ContaPessoaFisica;
import com.example.repository.ContaPessoaFisicaRepository;

@Service
public class ContaPessoaFisicaService extends ContaService {
	
	@Autowired
	private ContaPessoaFisicaRepository contaPessoaFisicaRepository;

	public ContaPessoaFisica insereContaPessoaFisica(ContaPessoaFisicaInsercaoDto contaPessoaFisicaInsercaoDto) {
		TipoConta tipoConta = contaPessoaFisicaInsercaoDto.getTipoConta();
		double saldo = contaPessoaFisicaInsercaoDto.getSaldo();
		String cpf = contaPessoaFisicaInsercaoDto.getCpf();
		
		super.validaTipoConta(tipoConta);
		super.validaSaldo(saldo);
		validaCpf(cpf, tipoConta);

		ContaPessoaFisica contaPessoaFisica = new ContaPessoaFisica(tipoConta, saldo, cpf);
		return contaPessoaFisicaRepository.save(contaPessoaFisica);
	}

	private void validaCpf(String cpf, TipoConta tipoConta) {
		if (cpf == null || cpf.isBlank()) {
			throw new ValidacaoException("CPF não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() < 11) {
			throw new ValidacaoException("CPF com menos de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cpf.length() > 11) {
			throw new ValidacaoException("CPF com mais de 11 caracteres.", HttpStatus.BAD_REQUEST);
		}
		
		Optional<ContaPessoaFisica> contaDigitalPessoaFisicaOptional = buscaContaDigital(cpf, tipoConta);
		if (contaDigitalPessoaFisicaOptional.isPresent()) {
			throw new ValidacaoException("Já existe uma conta " + tipoConta.getNome().toLowerCase()
					+ " cadastrada com o CPF " + cpf + ".", HttpStatus.CONFLICT);
		}
	}

	private Optional<ContaPessoaFisica> buscaContaDigital(String cpf, TipoConta tipoConta) {
		return contaPessoaFisicaRepository.findByCpfAndTipoConta(cpf, tipoConta);
	}
	
	public ContaPessoaFisicaBuscaDto1 buscaContaCompleta(String cpf, TipoConta tipoConta) {
		Optional<ContaPessoaFisica> contaPessoaFisicaOptional = buscaContaDigital(cpf, tipoConta);
		
		if (contaPessoaFisicaOptional.isPresent()) {
			return new ContaPessoaFisicaBuscaDto1(contaPessoaFisicaOptional.get());
		}
		
		return null;
	}

	public ContaPessoaFisica alteraContaPessoaFisica(ContaPessoaFisicaAlteracaoDto contaPessoaFisicaAlteracaoDto) {
		Long id = contaPessoaFisicaAlteracaoDto.getId();
		double saldo = contaPessoaFisicaAlteracaoDto.getSaldo();
		
		super.validaIdConta(id);
		
		Optional<ContaPessoaFisica> contaPessoaFisicaOptional = contaPessoaFisicaRepository.findById(id);
		
		ContaPessoaFisica contaPessoaFisicaOptionalSalvaBancoDados = contaPessoaFisicaOptional
				.orElseThrow(() -> new ValidacaoException("Não foi encontrada uma conta com o código informado.",
						HttpStatus.BAD_REQUEST));
		
		contaPessoaFisicaOptionalSalvaBancoDados.setSaldo(saldo);
		
		return contaPessoaFisicaRepository.save(contaPessoaFisicaOptionalSalvaBancoDados);
	}

	public void removeContaPessoaFisica(Long idContaPessoaFisica) {
		Optional<ContaPessoaFisica> enderecoOptional = contaPessoaFisicaRepository.findById(idContaPessoaFisica);
		
		ContaPessoaFisica contaPessoaFisicaSalvaBancoDados = enderecoOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o código informado.", HttpStatus.BAD_REQUEST));
		
		contaPessoaFisicaRepository.delete(contaPessoaFisicaSalvaBancoDados);
	}
}
