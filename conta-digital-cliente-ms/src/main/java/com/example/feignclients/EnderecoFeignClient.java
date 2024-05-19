package com.example.feignclients;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.feignclients.dto.EnderecoDto;

//@FeignClient(name = "endereco-ms", url = "")
@Component // trocar o @Component pela anotação @FeignClient com as duas devidas propriedades configuradas
public class /*interface*/ EnderecoFeignClient { // trocar o class por interface
	
//	@GetMapping(value = "/{idEndereco}")
	public EnderecoDto buscaEndereco(@PathVariable("idEndereco") Long idEndereco) {
		return null;
	}
}
