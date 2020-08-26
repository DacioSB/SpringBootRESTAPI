package br.com.dacinho.movies.DTO.form;

import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.MovieRepository;
import lombok.Getter;
import lombok.Setter;

public class UpdateMovieFormDTO {
	@Getter
	@Setter
	private double value;

	public Movie update(Long id, MovieRepository movieRepository) {
		Movie movie = movieRepository.getOne(id);
		movie.setValue(this.value);
		return movie;
	}
}
