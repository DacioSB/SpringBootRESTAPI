package br.com.dacinho.movies.DTO;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.dacinho.movies.models.Movie;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MovieDTO {
	private Long id;
	private String title;
	private double value;
	private String linkPoster;
	private double rating;
	private List<GenreDTO> genres = new ArrayList<>();
	
	public MovieDTO(Movie movie) {
		this.id = movie.getId();
		this.title = movie.getTitle();
		this.value = movie.getValue();
		this.linkPoster = movie.getLinkPoster();
		this.rating = movie.getRating();
		this.genres = GenreDTO.convert(movie.getGenres());
	}
	public static List<MovieDTO> convert(List<Movie> movies) {
		return movies.stream().map(MovieDTO::new).collect(Collectors.toList());
	}
	public static Set<MovieDTO> convert(Set<Movie> movies) {
		return movies.stream().map(MovieDTO::new).collect(Collectors.toSet());
	}
	public static Page<MovieDTO> convert(Page<Movie> movies) {
		return movies.map(MovieDTO::new);
	}

	
	
}
