package io.github.rimonmostafiz.component.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rimon Mostafiz
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
