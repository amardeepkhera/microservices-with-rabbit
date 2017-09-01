package microservices.rating.dao;

import microservices.rating.entity.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
public interface RatingRepository extends CrudRepository<Rating, String> {

    Optional<Rating> findById(String id);
}
