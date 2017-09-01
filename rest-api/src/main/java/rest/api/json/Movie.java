package rest.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by amardeep2551 on 8/19/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    private String id;
    private String name;
    private String directorName;
    private String genre;
    private Rating rating;
}
