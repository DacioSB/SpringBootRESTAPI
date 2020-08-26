package br.com.dacinho.movies.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dacinho.movies.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
	@Transactional()
	@ReadOnlyProperty
	Optional<Client> findByLogin(String login);
}
