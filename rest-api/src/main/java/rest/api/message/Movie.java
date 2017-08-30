package rest.api.message;

import lombok.Data;


@Data
public class Movie {

    private String id;
    private String name;
    private String directorName;
    private String genre;
}
