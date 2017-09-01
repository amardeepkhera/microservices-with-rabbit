package rest.api.repository;

import rest.api.message.BaseMessage;
import rest.api.message.Rating;

/**
 * Created by amardeep2551 on 8/31/2017.
 */
public interface RatingRepository {

    Rating create(Rating apply);

    Rating getById(String id);

    BaseMessage update(Rating rating);
}
