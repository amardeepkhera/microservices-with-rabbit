package microservices.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by amardeep2551 on 8/29/2017.
 */
@EnableRabbit
@Configuration
public class Config {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setAddresses("rabbit");
        return connectionFactory;
    }


    @Bean
    public Executor taskExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory movieListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                              SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setTaskExecutor(taskExecutor());
        factory.setMaxConcurrentConsumers(10);
        factory.setErrorHandler(errorHandler());
        factory.setDefaultRequeueRejected(false);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(t -> {
            t.printStackTrace();
            return true;
        });
    }

    @Bean
    public Queue requestQueue() {
        return new Queue("queue.movie");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("microservices");
    }

    @Bean
    public List<Binding> bindings() {
        return Arrays.asList(
                        BindingBuilder.bind(requestQueue()).to(directExchange()).with("movie.getById"),
                        BindingBuilder.bind(requestQueue()).to(directExchange()).with("movie.save"),
                        BindingBuilder.bind(requestQueue()).to(directExchange()).with("movie.updateRating"));
    }

}
