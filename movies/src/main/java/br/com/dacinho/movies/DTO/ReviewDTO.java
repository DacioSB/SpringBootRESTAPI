package br.com.dacinho.movies.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
	private Date date;
	@Setter
	@Getter
	private int likes;
	@Getter
	@Setter
	private String clientName;
	
	public ReviewDTO(Review review) {
		this.content = review.getContent();
		this.rating = review.getRating();
		this.date = review.getDate();
		this.likes = review.getLikes();
		this.clientName = review.getClient().getLogin();
		
	}
	public String date() {
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(this.date);
	}
	
	public static List<ReviewDTO> convert(List<Review> reviews) {
		return reviews.stream().map(ReviewDTO::new).collect(Collectors.toList());
	}
}
