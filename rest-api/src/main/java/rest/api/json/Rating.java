package rest.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rating {

    private String id;
    private String rating;
    private Date addedOn;
    private Date updatedOn;
}
