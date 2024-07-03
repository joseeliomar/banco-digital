package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.ContaPessoaJuridicaAlteracaoDto;
import com.example.dto.ContaPessoaJuridicaBuscaDto1;
import com.example.dto.ContaPessoaJuridicaInsercaoDto;
import com.example.enumeration.TipoConta;
import com.example.exception.ValidacaoException;
import com.example.model.ContaPessoaJuridica;
import com.example.repository.ContaPessoaJuridicaRepository;

@Service
public class ContaPessoaJuridicaService extends ContaService {

	@Autowired
	private ContaPessoaJuridicaRepository contaPessoaJuridicaRepository;
	
	public ContaPessoaJuridica insereContaPessoaJuridica(ContaPessoaJuridicaInsercaoDto contaPessoaJuridicaInsercaoDto) {
		TipoConta tipoConta = contaPessoaJuridicaInsercaoDto.getTipoConta();
		String cnpj = contaPessoaJuridicaInsercaoDto.getCnpj();
		
		super.validaTipoConta(tipoConta);
		validaCnpj(cnpj, tipoConta);

		ContaPessoaJuridica contaPessoaJuridica = new ContaPessoaJuridica(tipoConta, 0.0, cnpj);
		return contaPessoaJuridicaRepository.save(contaPessoaJuridica);
	}

	private void validaCnpj(String cnpj, TipoConta tipoConta) {
		if (cnpj == null || cnpj.isBlank()) {
			throw new ValidacaoException("CNPJ não informado.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() < 14) {
			throw new ValidacaoException("CNPJ com menos de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		if (cnpj.length() > 14) {
			throw new ValidacaoException("CNPJ com mais de 14 caracteres.", HttpStatus.BAD_REQUEST);
		}
		
		Optional<ContaPessoaJuridica> contaDigitalPessoaJuridicaOptional = buscaContaPessoaJuridica(cnpj, tipoConta);
		if (contaDigitalPessoaJuridicaOptional.isPresent()) {
			throw new ValidacaoException("Já existe uma conta " + tipoConta.getNome().toLowerCase()
					+ " cadastrada com o CNPJ " + cnpj + ".", HttpStatus.CONFLICT);
		}
	}

	private Optional<ContaPessoaJuridica> buscaContaPessoaJuridica(String cnpj, TipoConta tipoConta) {
		return contaPessoaJuridicaRepository.findByCnpjAndTipoConta(cnpj, tipoConta);
	}
	
	public ContaPessoaJuridicaBuscaDto1 buscaContaPessoaJuridicaCompleta(String cnpj, TipoConta tipoConta) {
		Optional<ContaPessoaJuridica> contaPessoaJuridicaOptional = buscaContaPessoaJuridica(cnpj, tipoConta);
		
		if (contaPessoaJuridicaOptional.isPresent()) {
			return new ContaPessoaJuridicaBuscaDto1(contaPessoaJuridicaOptional.get());
		}
		
		return null;
	}

	public ContaPessoaJuridica alteraContaPessoaJuridica(ContaPessoaJuridicaAlteracaoDto contaPessoaJuridicaAlteracaoDto) {
		Long id = contaPessoaJuridicaAlteracaoDto.getId();
		double saldo = contaPessoaJuridicaAlteracaoDto.getSaldo();
		
		super.validaIdConta(id);
		
		Optional<ContaPessoaJuridica> contaPessoaJuridicaOptional = contaPessoaJuridicaRepository.findById(id);
		
		ContaPessoaJuridica contaPessoaJuridicaOptionalSalvaBancoDados = contaPessoaJuridicaOptional
				.orElseThrow(() -> new ValidacaoException("Não foi encontrada uma conta com o código informado.",
						HttpStatus.BAD_REQUEST));
		
		contaPessoaJuridicaOptionalSalvaBancoDados.setSaldo(saldo);
		
		return contaPessoaJuridicaRepository.save(contaPessoaJuridicaOptionalSalvaBancoDados);
	}

	public void removeContaPessoaJuridica(Long idContaPessoaJuridica) {
		Optional<ContaPessoaJuridica> enderecoOptional = contaPessoaJuridicaRepository.findById(idContaPessoaJuridica);
		
		ContaPessoaJuridica contaPessoaJuridicaSalvaBancoDados = enderecoOptional.orElseThrow(
				() -> new ValidacaoException("Não foi encontrada uma conta com o código informado.", HttpStatus.BAD_REQUEST));
		
		contaPessoaJuridicaRepository.delete(contaPessoaJuridicaSalvaBancoDados);
	}
}