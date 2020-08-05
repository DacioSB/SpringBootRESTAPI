package br.com.dacinho.movies.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Movie implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	@ManyToMany(cascade = CascadeType.MERGE)
	//@JoinTable(name="movie_genres", joinColumns={@JoinColumn(name="movie_id")}, inverseJoinColumns= {@JoinColumn(name="genre_id")})
	private List<Genre> genres = new ArrayList<>();
	@Getter
	@Setter
	private String age;
	@Getter
	@Setter
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();
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
	@Setter
	private double rating;
	@Getter
	@Setter
	@Column(columnDefinition = "TEXT")
	@JsonProperty("Plot")
	private String plot;
	@Getter
	@Setter
	private double imdbRating;
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	
	@JsonSetter("Runtime")
	public void jSetDuration(String duration) {
		String[] set = duration.split(" ");
		this.duration = Integer.parseInt(set[0]);
	}
	@JsonSetter("Year")
	public void jSetYear(String year) {
		this.year = Integer.parseInt(year);
	}
	
	public double getRating() {
		if(this.reviews.size() == 0) {
			return 0;
		}else {
			
			int rt = 0;
			for(Review r : this.reviews) {
				rt += r.getRating();
			}
			this.rating = rt / (double)this.reviews.size();
			return this.rating;
		}
		
	}


	
}
