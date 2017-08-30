package microservices.movie.messaging;

import microservices.movie.JsonHelper;
import microservices.movie.messaging.message.Movie;
import microservices.movie.service.MovieService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Component
public class Processor {


    private final ConcurrentHashMap<String, ConsumerAndSupplier<Message, Message>> concurrentHashMap = new ConcurrentHashMap<>();

    private final MovieService movieService;
    private final JsonHelper jsonHelper;

    public Processor(MovieService movieService, JsonHelper jsonHelper) {
        this.movieService = movieService;
        this.jsonHelper = jsonHelper;
        concurrentHashMap.put("movie.getById", this::getMovieById);
        concurrentHashMap.put("movie.save", this::saveMovie);
    }

    public Message process(Message message) {
        return concurrentHashMap.get(message.getMessageProperties().getReceivedRoutingKey()).consumeAndSupply(message);
    }

    private Message getMovieById(Message message) {
        final String id = jsonHelper.fromJsonAsByteArray(message.getBody(), String.class);
        final microservices.movie.dto.Movie movie = movieService.getById(id);
        final Movie movieMessage = dtoToMessageMovieMapper().apply(movie);
        return MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(movieMessage))
                        .copyProperties(message.getMessageProperties())
                        .build();

    }

    private Message saveMovie(Message message) {
        final Movie movie = jsonHelper.fromJsonAsByteArray(message.getBody(), Movie.class);
        final microservices.movie.dto.Movie movieDto = messageToDtoMovieMapper().apply(movie);
        final String movieId = movieService.save(movieDto);
        return MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(movieId))
                        .copyProperties(message.getMessageProperties())
                        .build();
    }

    private Function<microservices.movie.dto.Movie, Movie> dtoToMessageMovieMapper() {
        return source -> {
            Movie movie = new Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            return movie;
        };
    }

    private Function<Movie, microservices.movie.dto.Movie> messageToDtoMovieMapper() {
        return source -> {
            microservices.movie.dto.Movie movie = new microservices.movie.dto.Movie();
            movie.setId(source.getId());
            movie.setName(source.getName());
            movie.setDirectorName(source.getDirectorName());
            movie.setGenre(source.getGenre());
            return movie;
        };
    }
}
