package br.com.dacinho.movies.DAO;

import java.util.ArrayList;
import java.util.List;

import br.com.dacinho.movies.models.Movie;

public class MovieDAO {
	private static final List<Movie> movies = new ArrayList<>();
	
	public void save(Movie movie) {
		getMovies().add(movie);
	}
	private Movie search(Movie movie) {
		Movie mu = null;
		for(Movie m : getMovies()) {
			if(m.equals(movie)) {
				return m;
			}
		}
		return mu;
	}
	public void remove(Movie movie) {
		Movie movieFind = search(movie);
		if(movieFind != null) {
			movies.remove(movieFind);
		}
	}
	public List<Movie> getMovies() {
		return movies;
	}
	
}
