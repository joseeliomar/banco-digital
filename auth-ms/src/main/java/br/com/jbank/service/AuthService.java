package br.com.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbank.entity.UserCredential;

@Service
public class AuthService {

//	@Autowired
//	private UserCredentialRepository repository;
	
	@Autowired
	private JwtService jwtService;
	
	public String saveUser(UserCredential userCredential) {
//		repository.save(userCredential);
		return "user added to the system";
	}
	
	public String generateToken(String cpf) {
		return jwtService.generateToken(cpf);
	}
	
//	public void validateToken(String token) {
//		jwtService.validateToken(token);
//	}
}
