package br.com.dacinho.movies.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dacinho.movies.DTO.GenreDTO;
import br.com.dacinho.movies.models.Genre;
import br.com.dacinho.movies.repository.GenreRepository;

//@Controller
@RestController
public class GenresController {
	
	@Autowired
	private GenreRepository genreRepository;
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping("/genres")
	public List<GenreDTO> listGenres(){
		List<Genre> genres = genreRepository.findAll();
		return GenreDTO.convert(genres);
	}
}
