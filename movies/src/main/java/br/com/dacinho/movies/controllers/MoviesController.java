package br.com.dacinho.movies.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.models.Genre;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.GenreRepository;
import br.com.dacinho.movies.repository.MovieRepository;

@RestController
@RequestMapping("/")
public class MoviesController {
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private GenreRepository genreRepository;
	
	@GetMapping
	public List<List<MovieDTO>> listMovies(){
		List<Genre> genres = genreRepository.findAll();
		List<List<MovieDTO>> movies = new ArrayList<>();
		for(Genre g : genres) {
			movies.add(MovieDTO.convert(g.getMovies()));
		}
		return movies;
		
//		List<Movie> movies = movieRepository.findAll();
//		return MovieDTO.convert(movies);
	}
	@GetMapping("/byGenre")
	public List<MovieDTO> listMoviesByGenre(String genre){
		if(genre == null) {
			List<Movie> movies = movieRepository.findAll();
			return MovieDTO.convert(movies);
		}else {
			genre = genre.trim().toLowerCase();
			List<Movie> movies = movieRepository.findByGenres_Name(genre);
			return MovieDTO.convert(movies);
		}
		
	}
}
