package microservices.movie.messaging;

import microservices.movie.JsonHelper;
import microservices.movie.exception.NotFoundException;
import microservices.movie.message.BaseMessage;
import microservices.movie.message.Movie;
import microservices.movie.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final ConcurrentHashMap<String, Function<Message, Message>> concurrentHashMap = new ConcurrentHashMap<>();

    private final MovieService movieService;
    private final JsonHelper jsonHelper;

    public Processor(MovieService movieService, JsonHelper jsonHelper) {
        this.movieService = movieService;
        this.jsonHelper = jsonHelper;
        concurrentHashMap.put("movie.getById", this::getMovieById);
        concurrentHashMap.put("movie.save", this::saveMovie);
        concurrentHashMap.put("movie.updateRating", this::updateMovieRating);
    }

    Message process(Message message) {
        try {
            return concurrentHashMap.get(message.getMessageProperties().getReceivedRoutingKey()).apply(message);
        } catch (NotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
            return MessageBuilder
                            .withBody(jsonHelper.toJsonAsByteArray(new BaseMessage("404", e.getMessage())))
                            .copyProperties(message.getMessageProperties())
                            .build();
        }
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
        Movie movieMessage = new Movie();
        movieMessage.setId(movieId);
        return MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(movieMessage))
                        .copyProperties(message.getMessageProperties())
                        .build();
    }

    private Message updateMovieRating(Message message) {
        final Movie movie = jsonHelper.fromJsonAsByteArray(message.getBody(), Movie.class);
        final microservices.movie.dto.Movie movieDto = messageToDtoMovieMapper().apply(movie);
        movieService.updateMovieRating(movieDto);
        return MessageBuilder.withBody(jsonHelper.toJsonAsByteArray(new BaseMessage("201", "")))
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
            movie.setRatingId(source.getRatingId());
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
            movie.setRatingId(source.getRatingId());
            return movie;
        };
    }
}
