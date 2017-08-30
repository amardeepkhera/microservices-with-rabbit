package rest.api.repository.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Repository;
import rest.api.JsonHelper;
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

    private final AsyncRabbitTemplate asyncRabbitTemplate;
    private final JsonHelper jsonHelper;

    public MovieRepositoryImpl(AsyncRabbitTemplate asyncRabbitTemplate, JsonHelper jsonHelper) {
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Movie get(final String id) {
        final Movie movie;
        Message message = MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(id))
                        .build();
        AsyncRabbitTemplate.RabbitMessageFuture messageRabbitConverterFuture = asyncRabbitTemplate
                        .sendAndReceive("microservices", "movie.getById", message);
        try {
            movie = jsonHelper.fromJsonAsByteArray(messageRabbitConverterFuture.get(5, TimeUnit.SECONDS).getBody(), Movie.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    @Override
    public String save(Movie movie) {
        final String id;
        Message message = MessageBuilder
                        .withBody(jsonHelper.toJsonAsByteArray(movie))
                        .build();
        AsyncRabbitTemplate.RabbitMessageFuture messageRabbitConverterFuture = asyncRabbitTemplate
                        .sendAndReceive("microservices", "movie.save", message);
        try {
            id = jsonHelper.fromJsonAsByteArray(messageRabbitConverterFuture.get(5, TimeUnit.SECONDS).getBody(), String.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
}
