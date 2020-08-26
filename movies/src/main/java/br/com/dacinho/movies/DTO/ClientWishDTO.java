package br.com.dacinho.movies.DTO;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.MovieRepository;

public class ClientWishDTO {
	
	public static Client updateWishList(Long clientId, Long movieId, ClientRepository clientRepository, MovieRepository movieRepository) {
		// TODO Auto-generated method stub
		Client client = clientRepository.getOne(clientId);
		Movie movie = movieRepository.getOne(movieId);
		client.insertWish(movie);
		
		return client;
	}

	public static void removeWishList(Long clientId, Long movieId, ClientRepository clientRepository,
			MovieRepository movieRepository) {
		// TODO Auto-generated method stub
		Client client = clientRepository.getOne(clientId);
		Movie movie = movieRepository.getOne(movieId);
		client.removeWish(movie);
	}
}
