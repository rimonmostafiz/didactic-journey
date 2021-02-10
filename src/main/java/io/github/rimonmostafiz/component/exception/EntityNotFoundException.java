package io.github.rimonmostafiz.component.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EntityNotFoundException extends RuntimeException {

    private HttpStatus status;
    private String field;

    public EntityNotFoundException(HttpStatus status, String field) {
        this.status = status;
        this.field = field;
    }

    public EntityNotFoundException(HttpStatus status, String field, String message) {
        super(message);
        this.status = status;
        this.field = field;
    }

    public EntityNotFoundException(HttpStatus status, String field, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.field = field;
    }
}
