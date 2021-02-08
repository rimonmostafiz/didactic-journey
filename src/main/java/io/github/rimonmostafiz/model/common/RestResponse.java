package io.github.rimonmostafiz.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Rimon Mostafiz
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> implements Serializable {
    private String message;

    private HttpStatus status;

    private StatusCode statusCode;

    private SuccessDetails<T> success;

    private ErrorDetails error;

    private WarningDetails warning;

    private PendingResult pending;

    public RestResponse() {
    }

    public RestResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestResponse(HttpStatus status, ErrorDetails error) {
        this.statusCode = StatusCode.ERROR;
        this.status = status;
        this.error = error;
    }

    public RestResponse(HttpStatus status, PendingResult pending) {
        this.statusCode = StatusCode.PENDING;
        this.status = status;
        this.pending = pending;
    }

    public RestResponse(HttpStatus status, SuccessDetails<T> success) {
        this.statusCode = StatusCode.SUCCESS;
        this.status = status;
        this.success = success;
    }

    public RestResponse(HttpStatus status, WarningDetails warning) {
        this.statusCode = StatusCode.WARNING;
        this.status = status;
        this.warning = warning;
    }
}
