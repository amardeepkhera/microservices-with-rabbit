package microservices.rating.exception;

/**
 * Created by amardeep2551 on 8/20/2017.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }
}
