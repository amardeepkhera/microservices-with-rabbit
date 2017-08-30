package rest.api;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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

    @Bean
    public Executor taskExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public Queue replyQueueForMovie() {
        return new Queue("reply.queue.movie");
    }

    @Bean
    public SimpleMessageListenerContainer rpcReplyMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueues(replyQueueForMovie());
        simpleMessageListenerContainer.setTaskExecutor(taskExecutor());
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        return simpleMessageListenerContainer;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(ConnectionFactory connectionFactory) {

        AsyncRabbitTemplate asyncRabbitTemplate = new AsyncRabbitTemplate(rabbitTemplate(connectionFactory),
                        rpcReplyMessageListenerContainer(connectionFactory),
                        "microservices" + "/" + "reply.movie.getById");
        return asyncRabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("microservices");
    }

    @Bean
    public List<Binding> bindings() {
        return Arrays.asList(
                        BindingBuilder.bind(replyQueueForMovie()).to(directExchange()).with("reply.movie.getById"));
    }
}
