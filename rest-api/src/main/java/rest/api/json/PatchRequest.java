package rest.api.json;

import lombok.Data;

/**
 * Created by amardeep2551 on 8/20/2017.
 */
@Data
public class PatchRequest {

    private String op ;
    private String path;
    private String value;
}
