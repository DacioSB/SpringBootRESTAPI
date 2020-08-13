package br.com.dacinho.movies.DTO;

import java.util.ArrayList;
import java.util.List;

import br.com.dacinho.movies.models.Movie;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DetailsMovieDTO {
	@Getter
	private String title;
	@Getter
	private int duration;
	@Getter
	private int year;
	@Getter
	private String age;
	@Getter
	private String cast;
	@Getter
	private double value;
	@Getter
	private String linkTrailer;
	@Getter
	private String linkPoster;
	@Getter
	private double rating;
	@Getter
	private String plot;
	@Getter
	private double imdbRating;
	@Getter
	private Long id;
	@Getter
	private List<GenreDTO> genres;
	@Getter
	private List<ReviewDTO> reviews;
	
	public DetailsMovieDTO(Movie movie) {
		this.plot = movie.getPlot();
		this.title = movie.getTitle();
		this.duration = movie.getDuration();
		this.year = movie.getYear();
		this.age = movie.getAge();
		this.cast = movie.getCast();
		this.value = movie.getValue();
		this.linkTrailer = movie.getLinkTrailer();
		this.linkPoster = movie.getLinkPoster();
		this.rating = movie.getRating();
		this.imdbRating = movie.getImdbRating();
		this.id = movie.getId();
		this.genres = new ArrayList<>();
		this.genres = GenreDTO.convert(movie.getGenres());
		this.reviews = new ArrayList<>();
		this.reviews = ReviewDTO.convert(movie.getReviews());
	}
}
