package br.com.dacinho.movies.controllers;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.dacinho.movies.DTO.ClientBuyDTO;
import br.com.dacinho.movies.DTO.ClientDTO;
import br.com.dacinho.movies.DTO.ClientDepositDTO;
import br.com.dacinho.movies.DTO.ClientWishDTO;
import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.DTO.form.ClientFormDTO;
import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.models.Profile;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.MovieRepository;
import br.com.dacinho.movies.security.ClientSS;
import br.com.dacinho.movies.security.UserService;

@RestController
@RequestMapping("/clients")
public class ClientController {
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private MovieRepository movieRepository;
	@GetMapping("/{id}")
	public ResponseEntity<ClientDTO> find(@PathVariable Long id){
		verifyRole(id);
		Optional<Client> client = this.clientRepository.findById(id);
		
		if(client.isPresent()) {
			return ResponseEntity.ok(new ClientDTO(client.get()));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	private void verifyRole(Long id) {
		ClientSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Profile.ADMIN) && !id.equals(user.getId())) {
			throw new IllegalArgumentException("Acesso negado");
		}
	}
	@PostMapping
	@Transactional
	public ResponseEntity<ClientDTO> cadastrar(@RequestBody @Valid ClientFormDTO form, UriComponentsBuilder uriBuilder) {
		Optional<Client> findByLogin = this.clientRepository.findByLogin(form.getLogin());
		if(findByLogin.isPresent()) {
			return ResponseEntity.badRequest().build();
		}else {
			Client client =	form.convert();
			this.clientRepository.save(client);
			
			URI uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.getId()).toUri();
			return ResponseEntity.created(uri).body(new ClientDTO(client));
		}
		
		
	}
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody @Valid ClientFormDTO form){
		this.verifyRole(id);
		
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			Optional<Client> findByLogin = this.clientRepository.findByLogin(form.getLogin());
			if(findByLogin.isPresent()) {
				return ResponseEntity.badRequest().build();
			}else {
				Client client = form.update(id, this.clientRepository);
				return ResponseEntity.ok(new ClientDTO(client));
			}
			
		}
		
		return ResponseEntity.notFound().build();
		
	}
	@PutMapping("/deposit/{id}")
	@Transactional
	public ResponseEntity<ClientDTO> deposit(@PathVariable Long id, @RequestBody ClientDepositDTO client){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			Client newClient = client.update(id, this.clientRepository);
			return ResponseEntity.ok(new ClientDTO(newClient));
			
		}
		return ResponseEntity.badRequest().build();
	}
	//AJAX, Sem atualizar pagina
	@PutMapping("wishlist/{id}")
	@Transactional
	public ResponseEntity<ClientDTO> addWishList(@RequestParam String movieId, @PathVariable Long id){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
				
		if(optional.isPresent()) {
			Optional<Movie> movie = this.movieRepository.findById(Long.parseLong(movieId));
			if(movie.isPresent()) {
				Client client = ClientWishDTO.updateWishList(id, Long.parseLong(movieId), this.clientRepository, this.movieRepository);
				return ResponseEntity.ok(new ClientDTO(client));
			}else {
				return ResponseEntity.badRequest().build();
			}
			
		}
		return ResponseEntity.badRequest().build();
	}
	@PutMapping("buy/{id}")
	@Transactional
	public ResponseEntity<ClientDTO> buy(@RequestParam String movieId, @PathVariable Long id){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
		if(optional.isPresent()) {
			Optional<Movie> movie = this.movieRepository.findById(Long.parseLong(movieId));
			if(movie.isPresent()) {
				Client client = ClientBuyDTO.buy(id, Long.parseLong(movieId), movieRepository, clientRepository);
				return ResponseEntity.ok(new ClientDTO(client));
			}else {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
		
	}
	@DeleteMapping("wishlist/{id}")
	@Transactional
	public ResponseEntity<?> removeWishListMovie(@PathVariable Long id, @RequestParam String movieId){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			Optional<Movie> movie = this.movieRepository.findById(Long.parseLong(movieId));
			if(movie.isPresent()) {
				ClientWishDTO.removeWishList(id, Long.parseLong(movieId), this.clientRepository, this.movieRepository);
				return ResponseEntity.ok().build();
			}else {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	@GetMapping("buy/{id}")
	public ResponseEntity<Page<MovieDTO>> showMovieList(@PathVariable Long id, @PageableDefault(sort = {"imdbRating"}, direction = Sort.Direction.DESC, page = 0, size = 10) Pageable page){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			Page<Movie> movies = movieRepository.findByClientMovies_Movies_Id(id, page);
			return ResponseEntity.ok().body(MovieDTO.convert(movies));
		}
		return ResponseEntity.notFound().build();
	}
	@GetMapping("wishlist/{id}")
	public ResponseEntity<Page<MovieDTO>> showWishList(@PathVariable Long id, @PageableDefault(sort = {"imdbRating"}, direction = Sort.Direction.DESC, page = 0, size = 10) Pageable page){
		this.verifyRole(id);
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			Page<Movie> movies = movieRepository.findByClientWishList_WishList_Id(id, page);
			return ResponseEntity.ok().body(MovieDTO.convert(movies));
		}
		return ResponseEntity.notFound().build();
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remove(@PathVariable Long id){
		
		Optional<Client> optional = this.clientRepository.findById(id);
		
		if(optional.isPresent()) {
			this.clientRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	
}
