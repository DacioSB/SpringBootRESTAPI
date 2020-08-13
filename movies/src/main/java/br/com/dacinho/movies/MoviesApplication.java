package br.com.dacinho.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dacinho.movies.DAO.MovieDAO;
import br.com.dacinho.movies.DTO.MovieFormDTO;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.repository.GenreRepository;
import br.com.dacinho.movies.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class MoviesApplication {
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
    	Map<String, Double> imdbList = new HashMap<>();
    	imdbList.put("tt6673612", 11.90);
    	imdbList.put("tt7975244", 11.90);
    	imdbList.put("tt3794354", 7.90);
    	//{"tt6673612", "tt7975244", "tt3794354"};
    	return args -> {
    		String url = "";
    		for(String s : imdbList.keySet()) {
    			url = String.format("http://www.omdbapi.com/?i=%s&plot=full&apikey=a5d7e4e8", s);
    			MovieFormDTO form = restTemplate.getForObject(url, MovieFormDTO.class);
    			Movie movie = form.convert(this.genreRepository);
    			movie.setValue(imdbList.get(s));
    			this.mdao.save(movie);
    		}
            
        };
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
