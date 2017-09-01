package rest.api.repository.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Repository;
import rest.api.JsonHelper;
import rest.api.message.BaseMessage;
import rest.api.message.Rating;
import rest.api.repository.RatingRepository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by amardeep2551 on 8/31/2017.
 */
@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private final AsyncRabbitTemplate ratingRabbitTemplate;
    private final JsonHelper jsonHelper;

    public RatingRepositoryImpl(AsyncRabbitTemplate ratingRabbitTemplate, JsonHelper jsonHelper) {
        this.ratingRabbitTemplate = ratingRabbitTemplate;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Rating getById(String id) {
        final Message requestMessage = MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(id)).build();
        AsyncRabbitTemplate.RabbitMessageFuture responseMessageFuture = ratingRabbitTemplate
                        .sendAndReceive("microservices", "rating.getById", requestMessage);
        try {
            return jsonHelper.fromJsonAsByteArray(responseMessageFuture.get(5, TimeUnit.SECONDS).getBody(), Rating.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BaseMessage update(Rating rating) {
        final Message requestMessage = MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(rating)).build();
        AsyncRabbitTemplate.RabbitMessageFuture responseMessageFuture = ratingRabbitTemplate
                        .sendAndReceive("microservices", "rating.update", requestMessage);
        try {
            return jsonHelper.fromJsonAsByteArray(responseMessageFuture.get(5, TimeUnit.SECONDS).getBody(), BaseMessage.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Rating create(Rating rating) {
        final Rating response;
        final Message requestMessage = MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(rating)).build();
        AsyncRabbitTemplate.RabbitMessageFuture messageFuture = ratingRabbitTemplate
                        .sendAndReceive("microservices", "rating.create", requestMessage);
        try {
            response = jsonHelper.fromJsonAsByteArray(messageFuture.get(5, TimeUnit.SECONDS).getBody(), Rating.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
