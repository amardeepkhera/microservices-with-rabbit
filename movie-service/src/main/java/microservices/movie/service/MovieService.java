package microservices.movie.service;


import microservices.movie.dto.Movie;

import java.util.Map;

/**
 * Created by amardeep2551 on 8/29/2017.
 */
public interface MovieService {

    Movie getById(String id);

    String save(Movie movie);

    void updateMovieRating(Movie movieDto);
}
