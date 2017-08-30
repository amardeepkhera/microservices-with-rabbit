package rest.api.json;

import lombok.Data;

/**
 * Created by amardeep2551 on 8/19/2017.
 */
@Data
public class Movie {

    private String id;
    private String name;
    private String directorName;
    private String genre;
}
