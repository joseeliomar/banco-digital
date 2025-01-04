package br.com.jbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private DigitalAccountService digitalAccountService;
	
	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		var digitalAccount = digitalAccountService.findDigitalAccountPasswordByCpf(cpf);
		return new CustomUserDetails(cpf, digitalAccount.senha());
	}
}
