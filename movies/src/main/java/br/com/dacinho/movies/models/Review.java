package br.com.dacinho.movies.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Review implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private int likes;
	private Date date;
	private int rating;
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	@ManyToOne
	private Client client;
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name="reviewlikes_clients", joinColumns={@JoinColumn(name="review_id")}, inverseJoinColumns= {@JoinColumn(name="client_id")})
	private Set<Client> likeClients;
	@ManyToOne
	private Movie movie;
	
	public Review(String content, int rating, Date date) {
		this.content = content;
		this.rating = rating;
		this.date = date;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes() {
		this.likes += 1;
	}
	public void dislike() {
		this.likes -= 1;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Set<Client> getLikeClients() {
		return likeClients;
	}
	public void setLikeClients(Set<Client> likeClients) {
		this.likeClients = likeClients;
	}
	public void removeClientLike(Client client) {
		this.likeClients.remove(client);
	}
	public void addClientLike(Client client) {
		this.likeClients.add(client);
	}
	
	
}
