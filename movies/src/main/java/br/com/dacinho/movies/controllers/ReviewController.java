package br.com.dacinho.movies.controllers;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.dacinho.movies.DTO.ReviewDTO;
import br.com.dacinho.movies.DTO.form.ReviewFormDTO;
import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.models.Profile;
import br.com.dacinho.movies.models.Review;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.MovieRepository;
import br.com.dacinho.movies.repository.ReviewRepository;
import br.com.dacinho.movies.security.ClientSS;
import br.com.dacinho.movies.security.UserService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	
	@PostMapping("/publish")
	@Transactional
	public ResponseEntity<ReviewDTO> publish(@RequestParam Long clientId, @RequestParam Long movieId, @RequestBody @Valid ReviewFormDTO form, UriComponentsBuilder uriBuilder) {
		this.verifyRole(clientId);
		Optional<Client> cOptional = this.clientRepository.findById(clientId);
		Optional<Movie> mOptional = this.movieRepository.findById(movieId);
		if(cOptional.isPresent() && mOptional.isPresent()) {
			Review review = form.publish(clientId, movieId, clientRepository, movieRepository, reviewRepository);
			URI uri = uriBuilder.path("/reviews/{id}").buildAndExpand(review.getId()).toUri();
			return ResponseEntity.created(uri).body(new ReviewDTO(review));
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	@GetMapping("/{id}")
	public ResponseEntity<ReviewDTO> showReview(@PathVariable Long id){
		Optional<Review> rev = this.reviewRepository.findById(id);
		if(rev.isPresent()) {
			return ResponseEntity.ok(new ReviewDTO(rev.get()));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PostMapping("/{id}")
	@Transactional
	public void like(@PathVariable Long id, @RequestParam Long cid, @RequestParam Long mid) {
		this.verifyRole(cid);
		Optional<Client> cOptional = this.clientRepository.findById(cid);
		Optional<Movie> mOptional = this.movieRepository.findById(mid);
		Optional<Review> rOptional = this.reviewRepository.findById(id);
		if(cOptional.isPresent() && mOptional.isPresent() && rOptional.isPresent()) {
			Review rev = rOptional.get();
			Optional<Review> likeOptional = this.reviewRepository.findByLikeClients_ReviewsLiked_Id(id);
			if(likeOptional.isPresent()) {
				rev.dislike();
				rev.removeClientLike(cOptional.get());
			}else {
				rev.setLikes();
				rev.addClientLike(cOptional.get());
			}
			
			
		}
	}
	private void verifyRole(Long id) {
		ClientSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Profile.ADMIN) && !id.equals(user.getId())) {
			throw new IllegalArgumentException("Acesso negado");
		}
	}
}
