package rest.api.exception;

/**
 * Created by amardeep2551 on 9/1/2017.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
