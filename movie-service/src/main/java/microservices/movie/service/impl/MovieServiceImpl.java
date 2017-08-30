package microservices.movie.service.impl;

import microservices.movie.dao.MovieRepository;
import microservices.movie.dto.Movie;
import microservices.movie.exception.NotFoundException;
import microservices.movie.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/29/2017.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie getById(String id) {
        return movieRepository.findById(id)
                        .map(entityToDtoMovieMapper())
                        .orElseThrow(() -> new NotFoundException("movie not found"));
    }

    @Override
    public String save(Movie movie) {
        return movieRepository.save(dtoToEntityMovieMapper()
                        .andThen(movieEntity -> {
                            movieEntity.setId(UUID.randomUUID().toString());
                            return movieEntity;
                        })
                        .apply(movie)).getId();
    }

    private Function<microservices.movie.entity.Movie, Movie> entityToDtoMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            return movie;
        };
    }

    private Function<Movie, microservices.movie.entity.Movie> dtoToEntityMovieMapper() {
        return source -> {
            microservices.movie.entity.Movie movie = new microservices.movie.entity.Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            movie.setAddedOn(new Date());
            return movie;
        };
    }
}
