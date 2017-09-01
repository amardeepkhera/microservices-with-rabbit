package rest.api.constant;

import java.util.Arrays;

/**
 * Created by amardeep2551 on 8/20/2017.
 */
public enum PatchOperation {

    ADD("add"), REMOVE("remove"), REPLACE("replace");

    private final String value;

    PatchOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PatchOperation forValue(String value) {
        return Arrays
                        .stream(PatchOperation.values())
                        .filter(po -> po.getValue().equals(value))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException());

    }
}
