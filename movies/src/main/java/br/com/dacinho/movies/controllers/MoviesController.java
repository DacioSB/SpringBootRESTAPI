package br.com.dacinho.movies.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.MovieRepository;

@RestController
public class MoviesController {
	@Autowired
	private MovieRepository movieRepository;
	
	@RequestMapping("/movies")
	public List<MovieDTO> listMovies(){
		List<Movie> movies = movieRepository.findAll();
		return MovieDTO.convert(movies);
	}
	@RequestMapping("/movies/category/")
	public List<MovieDTO> listMoviesByGenre(@RequestParam String genre){
		genre = genre.trim().toLowerCase();
		System.out.println(genre);
		List<Movie> movies = movieRepository.findByGenres_Name(genre);
		return MovieDTO.convert(movies);
	}
}
