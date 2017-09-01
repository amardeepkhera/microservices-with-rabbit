package microservices.movie.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by amardeep2551 on 9/1/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {

    private String status;
    private String message;
}
