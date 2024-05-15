package com.example.feignclients;

import org.springframework.web.bind.annotation.PathVariable;

import com.example.feignclients.dto.EnderecoDto;

//@FeignClient(name = "endereco-ms", url = "")
public interface EnderecoFeignClient {
	
//	@GetMapping(value = "/{idEndereco}")
	public EnderecoDto buscaEndereco(@PathVariable("idEndereco") Long idEndereco);
}
