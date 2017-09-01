package rest.api.service.impl;

import org.springframework.stereotype.Component;
import rest.api.exception.ResourceNotFoundException;
import rest.api.message.BaseMessage;

/**
 * Created by amardeep2551 on 9/1/2017.
 */
@Component
public class ResponseStatusValidator {

    void verifyAndThrowResourceNotFoundException(BaseMessage baseMessage) throws ResourceNotFoundException {

        if ("404".equals(baseMessage.getStatus())) {
            throw new ResourceNotFoundException(baseMessage.getMessage());
        }
    }
}
