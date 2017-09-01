package rest.api.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by amardeep2551 on 8/30/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Rating extends BaseMessage {

    private String id;
    private String movieId;
    private String rating;
    private Date addedOn;
    private Date updatedOn;
}
