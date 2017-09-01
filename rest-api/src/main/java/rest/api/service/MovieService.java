package rest.api.service;

import rest.api.dto.Movie;

import java.util.Map;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
public interface MovieService {

    Movie getMovie(String id);

    String save(Movie movie);

    void updateRating(Movie movie);
}
