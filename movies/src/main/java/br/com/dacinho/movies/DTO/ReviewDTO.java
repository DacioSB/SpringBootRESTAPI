package br.com.dacinho.movies.DTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.com.dacinho.movies.models.Review;
import lombok.Getter;
import lombok.Setter;

public class ReviewDTO {
	@Setter
	@Getter
	private String content;
	@Setter
	@Getter
	private int rating;
	@Setter
	@Getter
	private Date date;
	@Setter
	@Getter
	private int likes;
	
	public ReviewDTO(Review review) {
		this.content = review.getContent();
		this.rating = review.getRating();
		this.date = review.getDate();
		this.likes = review.getLikes();
	}
	
	public static List<ReviewDTO> convert(List<Review> reviews) {
		return reviews.stream().map(ReviewDTO::new).collect(Collectors.toList());
	}
}
