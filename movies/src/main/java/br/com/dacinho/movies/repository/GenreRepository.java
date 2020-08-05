package br.com.dacinho.movies.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dacinho.movies.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long>{
	Genre findByName(String genreName);
}
