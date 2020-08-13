package br.com.dacinho.movies.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Movie implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name="movie_genres", joinColumns={@JoinColumn(name="movie_id")}, inverseJoinColumns= {@JoinColumn(name="genre_id")})
	private List<Genre> genres = new ArrayList<>();
	@Getter
	@Setter
	private String age;
	@Getter
	@Setter
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();
	@Getter
	@Setter
	@Column(columnDefinition = "TEXT")
	private String cast;
	@Getter
	@Setter
	private double value;
	@Getter
	@Setter
	private String linkTrailer;
	@Getter
	@Setter
	private String linkPoster;
	@Setter
	private double rating;
	@Getter
	@Setter
	@Column(columnDefinition = "TEXT")
	private String plot;
	@Getter
	@Setter
	private double imdbRating;
	//TODO ta faltando o jackson do imdbrating
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	public Movie(String title, int duration, int year, List<Genre> genres, String age, String cast, double value,
			String linkTrailer, String linkPoster, String plot, double imdbRating) {
		this.title = title;
		this.duration = duration;
		this.year = year;
		this.genres = genres;
		this.age = age;
		this.cast = cast;
		this.value = value;
		this.linkTrailer = linkTrailer;
		this.linkPoster = linkPoster;
		this.plot = plot;
		this.imdbRating = imdbRating;
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
