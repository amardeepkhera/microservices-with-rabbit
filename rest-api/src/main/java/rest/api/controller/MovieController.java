package rest.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rest.api.json.Movie;
import rest.api.service.MovieService;

import java.net.URI;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/{movie_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Movie> getMovie(@PathVariable("movie_id") String movieId) {
        return ResponseEntity.ok(dtoToJsonMovieMapper().apply(movieService.getMovie(movieId)));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addMovie(@RequestBody Movie movie) {
        final String movieId = movieService.save(jsonToDtoMovieMapper().apply(movie));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{movie_id}")
                        .buildAndExpand(movieId).toUri();
        return ResponseEntity.created(location).build();
    }
    private Function<rest.api.dto.Movie, Movie> dtoToJsonMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            return movie;
        };
    }

    private Function<Movie, rest.api.dto.Movie> jsonToDtoMovieMapper() {
        return source -> {
            rest.api.dto.Movie movie = new rest.api.dto.Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            return movie;
        };
    }
}
