package br.com.dacinho.movies.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//, UserDetails
@Entity
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Client implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Getter
	private Long id;
	@Getter
	@Setter
	private String senha;
	@Column(unique = true)
	@Getter
	@Setter
	private String login;
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();
	@Getter
	@Setter
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name="client_wishlist", joinColumns={@JoinColumn(name="client_id")}, inverseJoinColumns= {@JoinColumn(name="movie_id")})
	private Set<Movie> wishList = new HashSet<>();
	@Getter
	@Setter
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name="client_movies", joinColumns={@JoinColumn(name="client_id")}, inverseJoinColumns= {@JoinColumn(name="movie_id")})
	private Set<Movie> movies = new HashSet<>();
	@Getter
	@Column(columnDefinition = "Decimal(10,2) default '0.0'")
	private double wallet;
	@Getter
	@Setter
	@ManyToMany(mappedBy="likeClients", fetch = FetchType.LAZY)
	private List<Review> reviewsLiked;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PROFILES")
	private Set<Integer> profiles = new HashSet<>();
	
	public Client() {
		this.addProfile(Profile.CLIENT);
	}
	public Client(String login, String senha) {
		this.login = login;
		this.senha = senha;
		this.addProfile(Profile.CLIENT);
	}
	public void deposit(double value) {
		this.wallet += value;
	}
	public void withdraw(double value) {
		if(value > this.wallet) {
			throw new IllegalArgumentException("You don't have enough funds to complete the operation");
		}else {
			this.wallet -= value;
		}
	}
	public void insertWish(Movie movie) {
		this.wishList.add(movie);
	}
	public void removeWish(Movie movie) {
		this.wishList.remove(movie);
	}
	public void insertMovie(Movie movie) {
		this.movies.add(movie);
	}
	public void removeMovie(Movie movie) {
		this.movies.add(movie);
	}
	public Set<Profile> getProfiles(){
		return this.profiles.stream().map(x -> Profile.toEnum(x)).collect(Collectors.toSet());
	}
	public void addProfile(Profile profile) {
		this.profiles.add(profile.getCod());
	}
	
}
