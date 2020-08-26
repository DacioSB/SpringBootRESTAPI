package br.com.dacinho.movies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import br.com.dacinho.movies.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	Page<Movie> findByGenres_Name(String genre, Pageable pag);
	Page<Movie> findByTitleContaining(String title, Pageable pag);
	Page<Movie> findByClientWishList_WishList_Id(Long id, Pageable pag);
	Page<Movie> findByClientMovies_Movies_Id(Long id, Pageable pag);
}
