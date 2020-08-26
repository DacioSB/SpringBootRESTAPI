package br.com.dacinho.movies.DTO.form;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.models.Review;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.MovieRepository;
import br.com.dacinho.movies.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewFormDTO {
	@NotNull @NotEmpty @Length(min=3)
	private String content;
	@NotNull
	private int rating;
	private Date date = new Date(System.currentTimeMillis());
	
	public ReviewFormDTO(Review review) {
		this.content = review.getContent();
		this.rating = review.getRating();
		this.date = review.getDate();
	}
	public static List<ReviewFormDTO> convert(List<Review> reviews) {
		return reviews.stream().map(ReviewFormDTO::new).collect(Collectors.toList());
	}
	public Review publish(Long cid, Long mid, ClientRepository crep, MovieRepository mrep, ReviewRepository rrep) {
		Client client = crep.getOne(cid);
		Movie movie = mrep.getOne(mid);
		Review review = new Review(this.content, this.rating, this.date);
		review.setClient(client);
		review.setMovie(movie);
		rrep.save(review);
		return review;
		
	}
	
}
