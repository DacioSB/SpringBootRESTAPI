package br.com.dacinho.movies.DTO;

import java.util.List;
import java.util.stream.Collectors;

import br.com.dacinho.movies.models.Genre;

public class GenreDTO {
	private String name;
	
	public GenreDTO(Genre genre) {
		this.name = genre.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public static List<GenreDTO> convert(List<Genre> genres) {
		return genres.stream().map(GenreDTO::new).collect(Collectors.toList());
	}
	
}
