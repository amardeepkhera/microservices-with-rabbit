package rest.api.service.impl;

import org.springframework.stereotype.Service;
import rest.api.dto.Movie;
import rest.api.repository.MovieRepository;
import rest.api.service.MovieService;

import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @Override
    public Movie getMovie(String id) {

        return messageToDtoMovieMapper().apply(movieRepository.get(id));

    }

    @Override
    public String save(Movie movie) {
        return movieRepository.save(dtoToMessageMovieMapper().apply(movie));
    }

    private Function<rest.api.message.Movie, Movie> messageToDtoMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
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
            return movie;
        };
    }
}
