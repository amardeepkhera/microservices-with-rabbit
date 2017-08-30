package microservices.movie.messaging;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@FunctionalInterface
public interface ConsumerAndSupplier<C, S> {
    S consumeAndSupply(C c);
}
