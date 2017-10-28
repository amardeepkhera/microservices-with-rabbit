package rest.api.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import rest.api.dto.Movie;
import rest.api.dto.Rating;
import rest.api.message.BaseMessage;
import rest.api.repository.RatingRepository;
import rest.api.service.RatingService;

import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/31/2017.
 */
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ResponseStatusValidator responseStatusValidator;

    public RatingServiceImpl(RatingRepository ratingRepository, ResponseStatusValidator responseStatusValidator) {
        this.ratingRepository = ratingRepository;
        this.responseStatusValidator = responseStatusValidator;
    }


    @Override
    @HystrixCommand(fallbackMethod = "returnEmptyRating")
    public Rating getById(String id) {
        final rest.api.message.Rating rating = ratingRepository.getById(id);
        return messageToDtoRatingMapper().apply(rating);
    }

    public Rating returnEmptyRating(String id) {

        return new Rating();
    }

    @Override
    public String createRating(String rating) {
        rest.api.message.Rating ratingMessage = new rest.api.message.Rating();
        ratingMessage.setRating(rating);
        return ratingRepository.create(ratingMessage).getId();
    }

    @Override
    public void update(Rating rating) {
        final BaseMessage baseMessage = ratingRepository.update(dtoToMessageRatingMapper().apply(rating));
        responseStatusValidator.verifyAndThrowResourceNotFoundException(baseMessage);
    }

    private Function<rest.api.message.Rating, Rating> messageToDtoRatingMapper() {
        return ratingMessage -> {
            Rating rating = new Rating();
            rating.setId(ratingMessage.getId());
            rating.setMovieId(ratingMessage.getMovieId());
            rating.setRating(ratingMessage.getRating());
            rating.setAddedOn(ratingMessage.getAddedOn());
            rating.setUpdatedOn(ratingMessage.getUpdatedOn());
            return rating;

        };
    }

    private Function<Rating, rest.api.message.Rating> dtoToMessageRatingMapper() {
        return ratingDto -> {
            rest.api.message.Rating rating = new rest.api.message.Rating();
            rating.setId(ratingDto.getId());
            rating.setMovieId(ratingDto.getMovieId());
            rating.setRating(ratingDto.getRating());
            rating.setAddedOn(ratingDto.getAddedOn());
            rating.setUpdatedOn(ratingDto.getUpdatedOn());
            return rating;

        };
    }
}
