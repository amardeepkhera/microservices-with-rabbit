package microservices.movie.message;

import lombok.Data;


@Data
public class Movie extends BaseMessage{

    private String id;
    private String name;
    private String directorName;
    private String genre;
    private String ratingId;
}
