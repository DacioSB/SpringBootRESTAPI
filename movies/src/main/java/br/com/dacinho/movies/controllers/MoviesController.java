package br.com.dacinho.movies.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dacinho.movies.DTO.DetailsMovieDTO;
import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.DTO.form.MovieFormDTO;
import br.com.dacinho.movies.DTO.form.UpdateMovieFormDTO;
import br.com.dacinho.movies.models.Genre;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.GenreRepository;
import br.com.dacinho.movies.repository.MovieRepository;

@RestController
@RequestMapping("/")
public class MoviesController {
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private GenreRepository genreRepository;
	//@RequestParam int page, @RequestParam int qtd
	//Lembrar de ordenar
	//Lembrar de so pegar 6 de cada genero
	@GetMapping
	@Cacheable(value = "listMoviesGenres")
	public List<Page<MovieDTO>> listMovies(){
		Pageable pag = PageRequest.of(0, 3, Sort.Direction.DESC, "imdbRating");
		List<Genre> genres = genreRepository.findAll();
		List<Page<MovieDTO>> movies = new ArrayList<>();
		//MovieDTO.convert(g.getMovies())
		for(Genre g : genres) {
			movies.add(MovieDTO.convert(movieRepository.findByGenres_Name(g.getName(), pag)));
		}
		return movies;
		
//		List<Movie> movies = movieRepository.findAll();
//		return MovieDTO.convert(movies);
		//Eu deveria ter uma pagina pra cada genre
		//E fazer paginação em cada um
		//Se possivel ordenar pela qtd de vendas de cada filme
	}
	@GetMapping("/movies")
	@Cacheable(value = "listMovies")
	public Page<MovieDTO> listMoviesByGenre(@RequestParam(required = false) String genre, @PageableDefault(sort = {"imdbRating"}, direction = Sort.Direction.DESC, page = 0, size = 10) Pageable page){
		//Pageable pag = PageRequest.of(page, qtd, Direction.DESC, sort);
		
		
		if(genre == null) {
			Page<Movie> movies = movieRepository.findAll(page);
			return MovieDTO.convert(movies);
		}else {
			genre = genre.trim().toLowerCase();
			Page<Movie> movies = movieRepository.findByGenres_Name(genre, page);
			return MovieDTO.convert(movies);
		}
		
	}
	@GetMapping("/search")
	@Cacheable(value = "searchMovies")
	public ResponseEntity<Page<MovieDTO>> searchMovies(@RequestParam String title) throws JsonMappingException, JsonProcessingException{
		//Se for null ou vazio, retorna pra a pagina inicial
		Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "imdbRating");
		Page<Movie> movies = movieRepository.findByTitleContaining(title, page);
		return ResponseEntity.ok().body(MovieDTO.convert(movies));
		
	}
	//Just for manager
	//TODO refactor these methods which i use the jsonnode and related
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/register")
	//, @RequestBody String page
	public ResponseEntity<JsonNode> register(@RequestBody String body) throws JsonMappingException, JsonProcessingException {
		String url;
		RestTemplate rest = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(body);
		JsonNode nameNode = actualObj.get("name");
		JsonNode pageNode = actualObj.get("page");
		String name = nameNode.textValue();
		
		if(pageNode == null) {
			url = "http://www.omdbapi.com/?s="+name+"&type=movie&apikey=a5d7e4e8";
			ResponseEntity<String> response = rest.getForEntity(url.toString(), String.class);
			JsonNode root = mapper.readTree(response.getBody());
			System.out.println(root.path("Search").asText());
			return ResponseEntity.ok().body(root.path("Search"));
		}else {
			String page = pageNode.textValue();
			url = String.format("http://www.omdbapi.com/?s=%s&page=%s&type=movie&apikey=a5d7e4e8", name, page);
			ResponseEntity<String> response = rest.getForEntity(url, String.class);
			JsonNode root = mapper.readTree(response.getBody());
			return ResponseEntity.ok().body(root.path("Search"));
		}
	}
	//Just for manager
	//"imdb id":... "value"...
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/register/saveMovie")
	@Transactional
	@CacheEvict(value = {"listMoviesGenres", "listMovies"}, allEntries = true)
	public ResponseEntity<MovieDTO> saveMovie(@RequestBody String body, UriComponentsBuilder uriBuilder) throws JsonMappingException, JsonProcessingException {
		String url;
		RestTemplate rest = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(body);
		String imdb = actualObj.get("imdbID").textValue();
		Movie movie = null;
		if(imdb != null) {
			url = String.format("http://www.omdbapi.com/?i=%s&plot=full&apikey=a5d7e4e8", imdb);
			MovieFormDTO form = rest.getForObject(url, MovieFormDTO.class);
			movie = form.convert(this.genreRepository);
			movie.setValue(Double.parseDouble(actualObj.get("value").textValue()));
			movieRepository.save(movie);
		}
		URI uri = uriBuilder.path("/movies/{id}").buildAndExpand(movie.getId()).toUri();
		return ResponseEntity.created(uri).body(new MovieDTO(movie));
		
	}
	
	@GetMapping("/movies/{id}")
	public ResponseEntity<DetailsMovieDTO> detail(@PathVariable Long id) {
		Optional<Movie> movie = this.movieRepository.findById(id);
		if(movie.isPresent()) {
			return ResponseEntity.ok(new DetailsMovieDTO(movie.get()));
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	//Just for manager
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/movies/{id}")
	@Transactional
	@CacheEvict(value = {"listMoviesGenres", "listMovies"}, allEntries = true)
	public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody UpdateMovieFormDTO movie){
		Optional<Movie> movieOpt = this.movieRepository.findById(id);
		//Movie updatedMovie = movie.update(id, this.movieRepository);
		
		if(movieOpt.isPresent()) {
			Movie updatedMovie = movie.update(id, this.movieRepository);
			return ResponseEntity.ok(new MovieDTO(updatedMovie));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/movies/{id}")
	@Transactional
	@CacheEvict(value = {"listMoviesGenres", "listMovies"}, allEntries = true)
	public ResponseEntity<?> remove(@PathVariable Long id){
		Optional<Movie> movieOpt = this.movieRepository.findById(id);
		
		if(movieOpt.isPresent()) {
			this.movieRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
}
