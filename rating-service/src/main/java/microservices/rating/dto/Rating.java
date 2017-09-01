package microservices.rating.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Data
public class Rating {

    private String id;
    private String movieId;
    private String rating;
    private Date addedOn;
    private Date updatedOn;
}
