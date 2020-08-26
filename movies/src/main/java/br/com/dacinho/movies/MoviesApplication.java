package br.com.dacinho.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dacinho.movies.DAO.MovieDAO;
import br.com.dacinho.movies.DTO.form.MovieFormDTO;
import br.com.dacinho.movies.models.Client;
import br.com.dacinho.movies.models.Movie;
import br.com.dacinho.movies.models.Profile;
import br.com.dacinho.movies.repository.ClientRepository;
import br.com.dacinho.movies.repository.GenreRepository;
import br.com.dacinho.movies.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
public class MoviesApplication {
	private MovieDAO mdao = new MovieDAO();
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate, GenreRepository genreRepository) throws Exception {
    	Map<String, Double> imdbList = new HashMap<>();
    	imdbList.put("tt6673612", 11.90);
    	imdbList.put("tt7975244", 11.90);
    	imdbList.put("tt3794354", 7.90);
    	imdbList.put("tt0372784", 7.90);
    	imdbList.put("tt7713068", 7.90);
    	imdbList.put("tt8579674", 11.90);
    	imdbList.put("tt7286456", 19.90);
    	imdbList.put("tt6146586", 11.90);
    	imdbList.put("tt1979376", 34.90);
    	imdbList.put("tt1396484", 5.90);
    	imdbList.put("tt3741700", 12.90);
    	imdbList.put("tt6139732", 34.90);
    	imdbList.put("tt1130884", 4.90);
    	//{"tt6673612", "tt7975244", "tt3794354"};
    	return args -> {
    		String url = "";
    		for(String s : imdbList.keySet()) {
    			url = String.format("http://www.omdbapi.com/?i=%s&plot=full&apikey=a5d7e4e8", s);
    			MovieFormDTO form = restTemplate.getForObject(url, MovieFormDTO.class);
    			Movie movie = form.convert(genreRepository);
    			movie.setValue(imdbList.get(s));
    			this.mdao.save(movie);
    		}
            
        };
    }
    
    @Bean
    CommandLineRunner init(MovieRepository repository) {
    	List<Movie> movies = this.mdao.getMovies();
    	return args ->{
    		//repository.deleteAll();
    		for(Movie m : movies) {
    			repository.save(m);
    			//System.out.println(m);
    		}
    	};
    }
    @Bean
    CommandLineRunner insertClient(ClientRepository repository) {
    	return args ->{
    		Client c1 = new Client("docinho", pe.encode("0202"));
        	c1.addProfile(Profile.ADMIN);
        	Client c2 = new Client("extrinha", pe.encode("evolu"));
        	repository.saveAll(Arrays.asList(c1, c2));
    	};	
    }
    


}
