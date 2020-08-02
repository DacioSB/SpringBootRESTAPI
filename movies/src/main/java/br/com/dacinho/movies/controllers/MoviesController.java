package br.com.dacinho.movies.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.models.Movie;

@Controller
@RestController
public class MoviesController {
	@RequestMapping("/movies")
	public List<MovieDTO> lista(){
		Movie movie = new Movie();
		movie.setTitle("dragao branco");
		movie.setAge("PG");
		movie.setCast("Jan Cloude Van Damme");
		movie.setDuration(108);
		movie.setId(1L);
		movie.setLinkTrailer("www.fodase.com");
		movie.setYear(2018);
		return MovieDTO.convert(Arrays.asList(movie, movie, movie));
	}
}
