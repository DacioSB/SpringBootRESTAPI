package br.com.dacinho.movies.DTO;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import br.com.dacinho.movies.models.Genre;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.GenreRepository;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieFormDTO {
	
	@JsonProperty("Title")
	@Getter
	@Setter
	private String title;
	@Getter
	@Setter
	private int duration;
	@Getter
	@Setter
	private int year;
	@Getter
	@Setter
	@JsonProperty("Rated")
	private String age;
	@JsonProperty("Actors")
	@Getter
	@Setter
	private String cast;
	@Getter
	@Setter
	private double value;
	@Getter
	@Setter
	private String linkTrailer;
	@Getter
	@Setter
	@JsonProperty("Poster")
	private String linkPoster;
	@Getter
	@Setter
	@JsonProperty("Plot")
	private String plot;
	@Getter
	@Setter
	private double imdbRating;
	@Getter
	@Setter
	private String[] genresNames;
	
	@JsonSetter("Runtime")
	public void jSetDuration(String duration) {
		String[] set = duration.split(" ");
		this.duration = Integer.parseInt(set[0]);
	}
	@JsonSetter("Genre")
	public void jSetGenres(String genres) {
		this.genresNames = genres.split(","); 
	}
	@JsonSetter("Year")
	public void jSetYear(String year) {
		this.year = Integer.parseInt(year);
	}
	@JsonSetter("imdbRating")
	public void jSetimdbRating(String rating) {
		this.imdbRating = Double.parseDouble(rating);
	}
	public Movie convert(GenreRepository repository) {
		List<Genre> genres = new ArrayList<>();
		for(String str : this.genresNames) {
			Genre genreFound = repository.findByName(str.toLowerCase().trim());
			//System.out.println(genreFound);
			if(genreFound != null) {
				genres.add(genreFound);
				
			}
		}
		
		return new Movie(this.title, this.duration, this.year, genres, this.age, this.cast, this.value,
				this.linkTrailer, this.linkPoster, this.plot, this.imdbRating);
	}
	
	
}
