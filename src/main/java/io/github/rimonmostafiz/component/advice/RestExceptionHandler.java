package io.github.rimonmostafiz.component.advice;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.component.exception.UserNotFoundException;
import io.github.rimonmostafiz.component.exception.ValidationException;
import io.github.rimonmostafiz.model.common.ErrorDetails;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "io.github.rimonmostafiz")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<ErrorDetails> errorDetailsList = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::translateToErrorDetails)
                .collect(Collectors.toList());

        var errorResponse = ResponseUtils.buildErrorRestResponse(HttpStatus.BAD_REQUEST, errorDetailsList);

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("error.malformed.json.request", null, locale);
        var error = ResponseUtils.buildErrorRestResponse(HttpStatus.BAD_REQUEST, null, message);
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("EntityNotFoundException found", ex);
        String field = ex.getField() != null
                ? messageSource.getMessage(ex.getField(), null, locale) : null;
        String message = ex.getMessage() != null
                ? messageSource.getMessage(ex.getMessage(), null, locale) : null;
        return ResponseUtils.buildErrorResponse(ex.getStatus(), field, message);
    }

    @ExceptionHandler(value = DisabledException.class)
    private ResponseEntity<?> handleDisabledException(DisabledException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("DisabledException found", ex);
        String field = messageSource.getMessage("user.username", null, locale);
        String message = messageSource.getMessage("error.account.disable", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, field, message);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    private ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("UserNotFoundException found", ex);
        String field = messageSource.getMessage("user.username", null, locale);
        String message = messageSource.getMessage("error.user.not.found", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.NOT_FOUND, field, message);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    private ResponseEntity<?> handleBadCredentialsException(UserNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("BadCredentialsException found", ex);
        String field = messageSource.getMessage("user.password", null, locale);
        String message = messageSource.getMessage("error.invalid.username.or.password", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, field, message);
    }

    @ExceptionHandler(value = ValidationException.class)
    private ResponseEntity<?> handleValidationException(ValidationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("ValidationException found", ex);
        String field = messageSource.getMessage(ex.getField(), null, locale);
        String message = messageSource.getMessage(ex.getMessage(), null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, field, message);
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<?> handleUnknownException(Exception ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("Generic exception: ", ex);
        String field = messageSource.getMessage("field.error", null, locale);
        return ResponseUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, field, ResponseUtils.INTERNAL_SERVER_ERROR);
    }

    private ErrorDetails translateToErrorDetails(ObjectError error) {
        log.warn("error: {}", error);
        String fieldName = ((FieldError) error).getField();
        Locale locale = LocaleContextHolder.getLocale();
        // Spring Validator does not set defaultMessage in field error
        // But JSR303 validator sets the message to default
        var errorMessage = Optional.ofNullable(error.getDefaultMessage())
                .map(msg -> messageSource.getMessage(msg, null, locale))
                .orElseGet(error::getDefaultMessage);
        log.warn("errorMessage: {}", errorMessage);
        return new ErrorDetails(fieldName, errorMessage);
    }
}
