package rest.api.service;

import rest.api.dto.Rating;

import java.util.Optional;

/**
 * Created by amardeep2551 on 8/31/2017.
 */
public interface RatingService {
    Rating getById(String id);

    String createRating(String rating);

    void update(Rating rating);
}
