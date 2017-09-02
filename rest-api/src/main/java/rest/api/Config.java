package rest.api;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@EnableRabbit
@Configuration
public class Config {

    @Value("${server.port}")
    private String port;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setAddresses("localhost:5672,localhost:5673,localhost:5674");
        return connectionFactory;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public Queue replyQueueForMovie() {
        return new Queue("reply.queue.movie-" + port);
    }

    @Bean
    public Queue replyQueueForRating() {
        return new Queue("reply.queue.rating-" + port);
    }

    @Bean
    public SimpleMessageListenerContainer movieReplyMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueues(replyQueueForMovie());
        simpleMessageListenerContainer.setTaskExecutor(taskExecutor());
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        return simpleMessageListenerContainer;
    }

    @Bean
    public SimpleMessageListenerContainer ratingReplyMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueues(replyQueueForRating());
        simpleMessageListenerContainer.setTaskExecutor(taskExecutor());
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        return simpleMessageListenerContainer;
    }

    @Bean
    public AsyncRabbitTemplate movieRabbitTemplate(ConnectionFactory connectionFactory) {
        return new AsyncRabbitTemplate(new RabbitTemplate(connectionFactory),
                        movieReplyMessageListenerContainer(connectionFactory),
                        "microservices" + "/" + "reply.movie");
    }

    @Bean
    public AsyncRabbitTemplate ratingRabbitTemplate(ConnectionFactory connectionFactory) {
        return new AsyncRabbitTemplate(new RabbitTemplate(connectionFactory),
                        ratingReplyMessageListenerContainer(connectionFactory),
                        "microservices" + "/" + "reply.rating");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("microservices");
    }

    @Bean
    public List<Binding> bindings() {
        return Arrays.asList(
                        BindingBuilder.bind(replyQueueForMovie()).to(directExchange()).with("reply.movie")
                        , BindingBuilder.bind(replyQueueForRating()).to(directExchange()).with("reply.rating")
        );
    }
}
