package io.github.rimonmostafiz.component.advice;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

/**
 * @author Rimon Mostafiz
 */
@Log4j2
@RestControllerAdvice(basePackages = "io.github.rimonmostafiz")
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        String field = ex.getField() != null
                ? messageSource.getMessage(ex.getField(), null, Locale.getDefault()) : null;
        String message = ex.getMessage() != null
                ? messageSource.getMessage(ex.getMessage(), null, Locale.getDefault()) : null;

        RestResponse<Object> errorRestResponse = Utils.buildErrorRestResponse(ex.getStatus(), field, message);
        return ResponseEntity.status(ex.getStatus()).body(errorRestResponse);
    }
}
