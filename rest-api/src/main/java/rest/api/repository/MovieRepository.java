package rest.api.repository;

import rest.api.message.Movie;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
public interface MovieRepository {
    Movie get(String id);

    String save(Movie movie);
}
