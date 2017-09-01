package rest.api.message;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Movie extends BaseMessage {

    private String id;
    private String name;
    private String directorName;
    private String genre;
    private String ratingId;
}
