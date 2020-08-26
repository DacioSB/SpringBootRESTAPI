package br.com.dacinho.movies.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserService {
	
	public static ClientSS authenticated() {
		try {
			return (ClientSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
		
	}
}
