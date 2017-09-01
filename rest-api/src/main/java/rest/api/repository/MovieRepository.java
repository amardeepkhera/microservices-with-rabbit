package rest.api.repository;

import rest.api.message.BaseMessage;
import rest.api.message.Movie;

import java.util.Map;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
public interface MovieRepository {
    Movie get(String id);

    Movie save(Movie movie);

    BaseMessage updateRating(Movie movie);
}
