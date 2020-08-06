package br.com.dacinho.movies.DTO;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.dacinho.movies.models.Genre;
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
	private List<String> genres = new ArrayList<>();
	
	public MovieDTO(Movie movie) {
		this.id = movie.getId();
		this.title = movie.getTitle();
		this.value = movie.getValue();
		this.linkPoster = movie.getLinkPoster();
		this.rating = movie.getRating();
		this.genres = movie.getGenres().stream().map(Genre::getName).collect(Collectors.toList());
	}
	public static List<MovieDTO> convert(List<Movie> movies) {
		return movies.stream().map(MovieDTO::new).collect(Collectors.toList());
	}


	
	
}
