package br.com.jbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbank.dto.AuthRequestDto;
import br.com.jbank.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/token")
	public ResponseEntity<String> getToken(@RequestBody AuthRequestDto AuthRequest) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(AuthRequest.cpf(), AuthRequest.password()));
		if (authenticate.isAuthenticated()) {
			return ResponseEntity.ok(authService.generateToken(AuthRequest.cpf()));
		} else {
			throw new RuntimeException("Invalid access. User is not authenticated");
		}
	}

//	@GetMapping("/validate")
//	public ResponseEntity<String> validateToken(@RequestParam String token) {
//		authService.validateToken(token);
//		return ResponseEntity.ok("Token is valid");
//	}
}
