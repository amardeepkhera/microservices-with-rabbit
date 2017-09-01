package microservices.rating.messaging;


import microservices.rating.JsonHelper;
import microservices.rating.dto.Rating;
import microservices.rating.exception.NotFoundException;
import microservices.rating.message.BaseMessage;
import microservices.rating.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Component
public class Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private final ConcurrentHashMap<String, Function<Message, Message>> concurrentHashMap = new ConcurrentHashMap<>();

    private final RatingService ratingService;
    private final JsonHelper jsonHelper;

    public Processor(RatingService ratingService, JsonHelper jsonHelper) {
        this.ratingService = ratingService;
        this.jsonHelper = jsonHelper;
        concurrentHashMap.put("rating.getById", this::getById);
        concurrentHashMap.put("rating.create", this::createRating);
        concurrentHashMap.put("rating.update", this::updateRating);
    }

    Message process(Message message) {
        try {
            return concurrentHashMap.get(message.getMessageProperties().getReceivedRoutingKey()).apply(message);
        } catch (NotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
            return MessageBuilder
                            .withBody(jsonHelper.toJsonAsByteArray(new BaseMessage("404", e.getMessage())))
                            .copyProperties(message.getMessageProperties())
                            .build();
        }
    }

    private Message getById(Message message) {
        final Rating rating = ratingService.getById(jsonHelper.fromJsonAsByteArray(message.getBody(), String.class));
        return MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(dtoToMessageRatingMapper().apply(rating)))
                        .copyProperties(message.getMessageProperties())
                        .build();
    }

    private Message createRating(Message message) {
        final microservices.rating.message.Rating rating = jsonHelper.fromJsonAsByteArray(message.getBody(),
                        microservices.rating.message.Rating.class);
        final String ratingId = ratingService.save(messageToDtoRatingMapper().apply(rating));
        microservices.rating.message.Rating responseMessage = new microservices.rating.message.Rating();
        responseMessage.setId(ratingId);
        return MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(responseMessage))
                        .copyProperties(message.getMessageProperties())
                        .build();
    }

    private Message updateRating(Message message) {
        final microservices.rating.message.Rating rating = jsonHelper.fromJsonAsByteArray(message.getBody(),
                        microservices.rating.message.Rating.class);
        ratingService.update(messageToDtoRatingMapper().apply(rating));
        return MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(new BaseMessage("201", "")))
                        .copyProperties(message.getMessageProperties())
                        .build();
    }

    private Function<microservices.rating.message.Rating, Rating> messageToDtoRatingMapper() {
        return ratingDto -> {
            Rating rating = new Rating();
            rating.setId(ratingDto.getId());
            rating.setMovieId(ratingDto.getMovieId());
            rating.setRating(ratingDto.getRating());
            rating.setAddedOn(ratingDto.getAddedOn());
            rating.setUpdatedOn(ratingDto.getUpdatedOn());
            return rating;

        };
    }

    private Function<Rating, microservices.rating.message.Rating> dtoToMessageRatingMapper() {
        return ratingDto -> {
            microservices.rating.message.Rating rating = new microservices.rating.message.Rating();
            rating.setId(ratingDto.getId());
            rating.setMovieId(ratingDto.getMovieId());
            rating.setRating(ratingDto.getRating());
            rating.setAddedOn(ratingDto.getAddedOn());
            rating.setUpdatedOn(ratingDto.getUpdatedOn());
            return rating;

        };
    }
}
