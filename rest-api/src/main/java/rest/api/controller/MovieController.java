package rest.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rest.api.constant.PatchOperation;
import rest.api.json.PatchRequest;
import rest.api.dto.Rating;
import rest.api.json.Movie;
import rest.api.service.MovieService;
import rest.api.service.RatingService;

import javax.validation.Valid;
import java.net.URI;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final RatingService ratingService;

    public MovieController(MovieService movieService
                    , RatingService ratingService
    ) {
        this.movieService = movieService;
        this.ratingService = ratingService;
    }

    @GetMapping(value = "/{movie_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Movie> getMovie(@PathVariable("movie_id") String movieId) {
        final rest.api.dto.Movie movieDto = movieService.getMovie(movieId);
        return ResponseEntity.ok(dtoToJsonMovieMapper()
                        .andThen(movie -> {
                            if (movie.getRating() != null) {
                                movie.setRating(dtoToJsonRatingMapper().apply(ratingService.getById(movieDto.getRatingId())));
                            }
                            return movie;
                        }).apply(movieDto));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addMovie(@RequestBody @Valid Movie movie) {
        final String movieId = movieService.save(jsonToDtoMovieMapper().apply(movie));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{movie_id}")
                        .buildAndExpand(movieId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping(value = "/{movie_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateMovie(@PathVariable("movie_id") String movieId, @RequestBody PatchRequest patchRequest) {
        switch (PatchOperation.forValue(patchRequest.getOp())) {
            case REPLACE:
                switch (patchRequest.getPath()) {
                    case "/rating":
                        updateRating(movieId, patchRequest);
                        break;
                }
        }
        return ResponseEntity.noContent().build();
    }

    private void updateRating(@PathVariable("movie_id") String movieId, @RequestBody PatchRequest patchRequest) {
        final rest.api.dto.Movie movie = movieService.getMovie(movieId);
        if (movie.getRatingId() == null) {
            final String ratingId = ratingService.createRating(patchRequest.getValue());
            movie.setRatingId(ratingId);
            movie.setId(movieId);
            movieService.updateRating(movie);
        } else {
            Rating rating = new Rating();
            rating.setId(movie.getRatingId());
            rating.setRating(patchRequest.getValue());
            ratingService.update(rating);
        }
    }

    private Function<rest.api.dto.Movie, Movie> dtoToJsonMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            if (source.getRatingId() != null) {
                rest.api.json.Rating rating = new rest.api.json.Rating();
                rating.setId(source.getId());
                movie.setRating(rating);
            }
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

    private Function<Rating, rest.api.json.Rating> dtoToJsonRatingMapper() {
        return ratingDto -> {
            rest.api.json.Rating rating = new rest.api.json.Rating();
            rating.setId(ratingDto.getId());
            rating.setRating(ratingDto.getRating());
            rating.setAddedOn(ratingDto.getAddedOn());
            rating.setUpdatedOn(ratingDto.getUpdatedOn());
            return rating;

        };
    }
}
