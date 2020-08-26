package br.com.dacinho.movies.DTO;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.MovieRepository;

public class ClientBuyDTO {
	
	public static Client buy(Long clientId, Long movieId, MovieRepository movieRepository, ClientRepository clientRepository) {
		Client client = clientRepository.getOne(clientId);
		Movie movie = movieRepository.getOne(movieId);
		
		if(client.getMovies().contains(movie)) {
			return client;
		}else {
			client.withdraw(movie.getValue());
			if(client.getWishList().contains(movie)) {
				client.removeWish(movie);
				client.insertMovie(movie);
				return client;
			}else {
				client.insertMovie(movie);
				return client;
			}
		}
	}
}
