package rest.api.service.impl;

import org.springframework.stereotype.Service;
import rest.api.dto.Movie;
import rest.api.message.BaseMessage;
import rest.api.repository.MovieRepository;
import rest.api.service.MovieService;

import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ResponseStatusValidator responseStatusValidator;

    public MovieServiceImpl(MovieRepository movieRepository, ResponseStatusValidator responseStatusValidator) {
        this.movieRepository = movieRepository;
        this.responseStatusValidator = responseStatusValidator;
    }


    @Override
    public Movie getMovie(String id) {
        rest.api.message.Movie movie = movieRepository.get(id);
        responseStatusValidator.verifyAndThrowResourceNotFoundException(movie);
        return messageToDtoMovieMapper().apply(movie);
    }

    @Override
    public String save(Movie movie) {
        return movieRepository.save(dtoToMessageMovieMapper().apply(movie)).getId();
    }

    @Override
    public void updateRating(Movie movie) {
        BaseMessage baseMessage = movieRepository.updateRating(dtoToMessageMovieMapper().apply(movie));
        responseStatusValidator.verifyAndThrowResourceNotFoundException(baseMessage);
    }

    private Function<rest.api.message.Movie, Movie> messageToDtoMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            movie.setRatingId(source.getRatingId());
            return movie;
        };
    }

    private Function<Movie, rest.api.message.Movie> dtoToMessageMovieMapper() {
        return source -> {
            rest.api.message.Movie movie = new rest.api.message.Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            movie.setRatingId(source.getRatingId());
            return movie;
        };
    }
}
