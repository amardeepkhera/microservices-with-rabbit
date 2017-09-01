package rest.api.repository.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Repository;
import rest.api.JsonHelper;
import rest.api.message.BaseMessage;
import rest.api.message.Movie;
import rest.api.repository.MovieRepository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Repository
public class MovieRepositoryImpl implements MovieRepository {

    private final AsyncRabbitTemplate movieRabbitTemplate;
    private final JsonHelper jsonHelper;

    public MovieRepositoryImpl(AsyncRabbitTemplate movieRabbitTemplate, JsonHelper jsonHelper) {
        this.movieRabbitTemplate = movieRabbitTemplate;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Movie get(final String id) {
        final Movie movie;
        Message message = MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(id))
                        .build();
        AsyncRabbitTemplate.RabbitMessageFuture messageRabbitConverterFuture = movieRabbitTemplate
                        .sendAndReceive("microservices", "movie.getById", message);
        try {
            movie = jsonHelper.fromJsonAsByteArray(messageRabbitConverterFuture.get(5, TimeUnit.SECONDS).getBody(), Movie.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    @Override
    public Movie save(Movie movie) {
        final Movie responseMessage;
        Message message = MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(movie))
                        .build();
        AsyncRabbitTemplate.RabbitMessageFuture messageRabbitConverterFuture = movieRabbitTemplate
                        .sendAndReceive("microservices", "movie.save", message);
        try {
            responseMessage = jsonHelper.fromJsonAsByteArray(messageRabbitConverterFuture.get(5, TimeUnit.SECONDS).getBody(), Movie.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return responseMessage;
    }

    @Override
    public BaseMessage updateRating(Movie movie) {
        final BaseMessage baseMessage;
        final Message requestMessage = MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(movie)).build();
        AsyncRabbitTemplate.RabbitMessageFuture responseMessageuture = movieRabbitTemplate
                        .sendAndReceive("microservices", "movie.updateRating", requestMessage);
        try {
            baseMessage = jsonHelper.fromJsonAsByteArray(responseMessageuture.get(5, TimeUnit.SECONDS).getBody(), BaseMessage.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return baseMessage;
    }
}
