package br.com.dacinho.movies.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Title")
	private String title;
	private int duration;
	private int year;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Genre> genres;
	private String age;
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();
	@JsonProperty("Actors")
	private String cast;
	private double value;
	private String linkTrailer;
	@JsonProperty("Poster")
	private String linkPoster;
	private double rating;
	@JsonProperty("Plot")
	private String plot;
	private double imdbRating;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getLinkTrailer() {
		return linkTrailer;
	}
	public void setLinkTrailer(String linkTrailer) {
		this.linkTrailer = linkTrailer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@JsonSetter("Runtime")
	public void jSetDuration(String duration) {
		String[] set = duration.split(" ");
		this.duration = Integer.parseInt(set[0]);
	}
	@JsonSetter("Year")
	public void jSetYear(String year) {
		this.year = Integer.parseInt(year);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public String getLinkPoster() {
		return linkPoster;
	}
	public void setLinkPoster(String linkPoster) {
		this.linkPoster = linkPoster;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public double getImdbRating() {
		return imdbRating;
	}
	public void setImdbRating(double imdbRating) {
		this.imdbRating = imdbRating;
	}
	@Override
	public String toString() {
		return this.title + "-" + this.year + "-" + this.duration;
	}
	
}
