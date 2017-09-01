package rest.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by amardeep2551 on 8/19/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    private String id;
    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    @NotNull
    @Size(min = 1, max = 100)
    private String directorName;
    @NotNull
    @Size(min = 1, max = 100)
    private String genre;
    private Rating rating;
}
