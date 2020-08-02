package br.com.dacinho.movies.DTO;



import java.util.List;
import java.util.stream.Collectors;

import br.com.dacinho.movies.models.Movie;

public class MovieDTO {
	private Long id;
	private String title;
	private double value;
	private String linkPoster;
	private double rating;
	
	public MovieDTO(Movie movie) {
		this.id = movie.getId();
		this.title = movie.getTitle();
		this.value = movie.getValue();
		this.linkPoster = movie.getLinkPoster();
		this.rating = movie.getRating();
	}
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public double getValue() {
		return value;
	}
	public String getLinkPoster() {
		return linkPoster;
	}
	public double getRating() {
		return rating;
	}
	public static List<MovieDTO> convert(List<Movie> movies) {
		return movies.stream().map(MovieDTO::new).collect(Collectors.toList());
	}
	
	
}
