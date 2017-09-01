package rest.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by amardeep2551 on 9/1/2017.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.name(), exception.getMessage()));
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String code;
        private String message;
    }

}
