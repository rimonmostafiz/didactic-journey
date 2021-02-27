package io.github.rimonmostafiz.model.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.rimonmostafiz.component.exception.ValidationException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    ACTIVE,
    INACTIVE,
    DELETED;

    public static Status getStatus(String status) {
        return Arrays.stream(Status.values())
                .filter(v -> v.name().equals(status))
                .findFirst()
                .orElseThrow(() -> new ValidationException(HttpStatus.BAD_REQUEST, "status", "error.project.invalid.status"));
    }
}
