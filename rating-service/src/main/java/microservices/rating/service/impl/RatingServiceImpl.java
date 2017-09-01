package microservices.rating.service.impl;

import microservices.rating.dao.RatingRepository;
import microservices.rating.dto.Rating;
import microservices.rating.exception.NotFoundException;
import microservices.rating.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating getById(String id) {
        return ratingRepository.findById(id)
                        .map(entityToDtoRatingMapper())
                        .orElseThrow(() -> new NotFoundException("Not found"));
    }

    @Override
    public void update(Rating rating) {
        Optional<microservices.rating.entity.Rating> ratingEntityOptional = ratingRepository.findById(rating.getId());
        if (ratingEntityOptional.isPresent()) {
            microservices.rating.entity.Rating ratingEntity = ratingEntityOptional.get();
            ratingEntity.setRating(rating.getRating());
            ratingEntity.setUpdatedOn(new Date());
            ratingRepository.save(ratingEntity);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    @Override
    public String save(Rating rating) {
        microservices.rating.entity.Rating ratingEntity = new microservices.rating.entity.Rating();
        ratingEntity.setId(UUID.randomUUID().toString());
        ratingEntity.setRating(rating.getRating());
        ratingEntity.setAddedOn(new Date());
        return ratingRepository.save(ratingEntity).getId();
    }

    private Function<microservices.rating.entity.Rating, Rating> entityToDtoRatingMapper() {
        return ratingEntity -> {
            Rating rating = new Rating();
            rating.setId(ratingEntity.getId());
            rating.setRating(ratingEntity.getRating());
            rating.setAddedOn(ratingEntity.getAddedOn());
            rating.setUpdatedOn(ratingEntity.getUpdatedOn());
            return rating;

        };
    }


}
