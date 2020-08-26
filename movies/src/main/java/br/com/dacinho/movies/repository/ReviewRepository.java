package br.com.dacinho.movies.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dacinho.movies.models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	Optional<Review> findByLikeClients_ReviewsLiked_Id(Long id);

}
