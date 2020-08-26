package br.com.dacinho.movies.DTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.dacinho.movies.models.Client;
import lombok.Getter;

@Getter
public class ClientDTO {
	
	private String login;
	private Set<MovieDTO> wishList = new HashSet<>();
	private Set<MovieDTO> movies = new HashSet<>();
	private double wallet;
	
	public ClientDTO(Client client) {
		this.login = client.getLogin();
		this.wishList = MovieDTO.convert(client.getWishList());
		this.movies = MovieDTO.convert(client.getMovies());
		this.wallet = client.getWallet();
	}
	public static List<ClientDTO> convert(List<Client> clients) {
		return clients.stream().map(ClientDTO::new).collect(Collectors.toList());
	}
	
}
