package microservices.rating.service;

import microservices.rating.dto.Rating;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
public interface RatingService {
    String save(Rating rating);

    Rating getById(String id);

    void update(Rating rating);
}
