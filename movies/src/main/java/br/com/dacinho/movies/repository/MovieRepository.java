package br.com.dacinho.movies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.dacinho.movies.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	List<Movie> findByGenres_Name(String genre);
}
