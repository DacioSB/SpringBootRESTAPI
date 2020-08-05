package br.com.dacinho.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import br.com.dacinho.movies.DAO.MovieDAO;
import br.com.dacinho.movies.models.Genre;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.GenreRepository;
import br.com.dacinho.movies.repository.MovieRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class MoviesApplication {
	private static final Logger log = LoggerFactory.getLogger(MoviesApplication.class);
	private MovieDAO mdao = new MovieDAO();
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}
	//tt3794354
	//tt7975244
	//tt6673612
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
    	String[] imdbList = {"tt6673612", "tt7975244", "tt3794354"};
    	return args -> {
    		String url = "";
    		for(String s : imdbList) {
    			url = String.format("http://www.omdbapi.com/?i=%s&plot=full&apikey=a5d7e4e8", s);
    			Movie movie = restTemplate.getForObject(
                        url, Movie.class);
    			addGenre(restTemplate, url, movie);
    			this.mdao.save(movie);
                log.info("Resultado da Chamada REST: " + movie.toString());
    		}
            
        };
    }
	private void addGenre(RestTemplate restTemplate, String url, Movie movie)
			throws JsonProcessingException, JsonMappingException {
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		String[] genre = root.path("Genre").asText().split(",");
		for(String str : genre) {
			System.out.println("!!!!!!!!!!!!!!!");
			System.out.println(str);
			System.out.println("----------");
			Genre genreFound = this.genreRepository.findByName(str.toLowerCase().trim());
			System.out.println(genreFound);
			if(genreFound != null) {
				System.out.println(movie.getGenres());
				movie.getGenres().add(genreFound);
			}
		}
	}
    @Bean
    CommandLineRunner init(MovieRepository repository) {
    	List<Movie> movies = this.mdao.getMovies();
    	return args ->{
    		repository.deleteAll();
    		for(Movie m : movies) {
    			repository.save(m);
    			//System.out.println(m);
    		}
    	};
    }
    


}
