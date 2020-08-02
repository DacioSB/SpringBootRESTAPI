package br.com.dacinho.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import br.com.dacinho.movies.models.Movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class MoviesApplication {
	private static final Logger log = LoggerFactory.getLogger(MoviesApplication.class);
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
        return args -> {
            Movie movie = restTemplate.getForObject(
                    "http://www.omdbapi.com/?i=tt6673612&plot=full&apikey=a5d7e4e8", Movie.class);
            log.info("Resultado da Chamada REST: " + movie.toString());
        };
    }

}
