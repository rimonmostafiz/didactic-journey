package io.github.rimonmostafiz.component.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author Rimon Mostafiz
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationException extends RuntimeException {
    private HttpStatus status;
    private String field;

    public ValidationException(HttpStatus status, String field) {
        this.status = status;
        this.field = field;
    }

    public ValidationException(HttpStatus status, String field, String message) {
        super(message);
        this.status = status;
        this.field = field;
    }

    public ValidationException(HttpStatus status, String field, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.field = field;
    }
}
