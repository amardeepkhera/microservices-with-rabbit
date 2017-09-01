package microservices.rating.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by amardeep2551 on 8/29/2017.
 */
@Component
public class Listener {

    private final Processor processor;

    public Listener(Processor processor) {
        this.processor = processor;
    }

    @RabbitHandler
    @RabbitListener(containerFactory = "ratingListenerContainerFactory", queues = {"queue.rating"})
    public Message onMessage(Message message) {
        return processor.process(message);
    }
}
