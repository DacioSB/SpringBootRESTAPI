package br.com.dacinho.movies.security;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.repository.ClientRepository;
@Service
public class AutSecurity implements UserDetailsService{
	
	@Autowired
	private ClientRepository repository;
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Optional<Client> opt = this.repository.findByLogin(login);
		if(!opt.isPresent()) {
			throw new UsernameNotFoundException(login);
		}else {
			Client client = opt.get();
			return new ClientSS(client.getId(),
					client.getLogin(),
					client.getSenha(),
					client.getProfiles()
					.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList()));
		}
		
	}

}
