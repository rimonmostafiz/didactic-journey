package io.github.rimonmostafiz.component.advice;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.component.exception.UserNotFoundException;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "io.github.rimonmostafiz")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        String field = ex.getField() != null
                ? messageSource.getMessage(ex.getField(), null, Locale.getDefault()) : null;
        String message = ex.getMessage() != null
                ? messageSource.getMessage(ex.getMessage(), null, Locale.getDefault()) : null;

        RestResponse<Object> errorRestResponse = ResponseUtils.buildErrorRestResponse(ex.getStatus(), field, message);
        return ResponseEntity.status(ex.getStatus()).body(errorRestResponse);
    }

    @ExceptionHandler(value = DisabledException.class)
    private ResponseEntity<?> handleDisabledException(DisabledException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("DisabledException found", ex);
        String field = messageSource.getMessage("user.username", null, locale);
        String message = messageSource.getMessage("error.account.disable", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, field, message);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    private ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("UserNotFoundException found", ex);
        String field = messageSource.getMessage("user.username", null, locale);
        String message = messageSource.getMessage("error.user.not.found", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, field, message);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    private ResponseEntity<?> handleBadCredentialsException(UserNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("BadCredentialsException found", ex);
        String field = messageSource.getMessage("user.password", null, locale);
        String message = messageSource.getMessage("error.invalid.username.password", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, field, message);
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<?> handleUnknownException(Exception ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("Error while login", ex);
        String field = messageSource.getMessage("field.error", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, field, ResponseUtils.INTERNAL_SERVER_ERROR);
    }
}
