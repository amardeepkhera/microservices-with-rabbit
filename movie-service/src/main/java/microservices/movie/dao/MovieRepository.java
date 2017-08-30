package microservices.movie.dao;


import microservices.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by amardeep2551 on 8/19/2017.
 */

public interface MovieRepository extends CrudRepository<Movie, String> {

    Page<Movie> findAll(Pageable pageable);

    Optional<Movie> findById(String id);
}
