package br.com.dacinho.movies.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dacinho.movies.DTO.MovieDTO;
import br.com.dacinho.movies.DTO.MovieFormDTO;
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
	
	@GetMapping
	public List<List<MovieDTO>> listMovies(){
		List<Genre> genres = genreRepository.findAll();
		List<List<MovieDTO>> movies = new ArrayList<>();
		for(Genre g : genres) {
			movies.add(MovieDTO.convert(g.getMovies()));
		}
		return movies;
		
//		List<Movie> movies = movieRepository.findAll();
//		return MovieDTO.convert(movies);
	}
	@GetMapping("/byGenre")
	public List<MovieDTO> listMoviesByGenre(String genre){
		if(genre == null) {
			List<Movie> movies = movieRepository.findAll();
			return MovieDTO.convert(movies);
		}else {
			genre = genre.trim().toLowerCase();
			List<Movie> movies = movieRepository.findByGenres_Name(genre);
			return MovieDTO.convert(movies);
		}
		
	}
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
	//"imdb id":... "value"...
	@PostMapping("/register/saveMovie")
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
		URI uri = uriBuilder.path("/register/saveMovie/{id}").buildAndExpand(movie.getId()).toUri();
		return ResponseEntity.created(uri).body(new MovieDTO(movie));
		
	}
	
}
