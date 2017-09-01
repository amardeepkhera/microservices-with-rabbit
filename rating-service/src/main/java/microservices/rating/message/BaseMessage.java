package microservices.rating.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by amardeep2551 on 9/1/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage {

    private String status;
    private String message;
}
