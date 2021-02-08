package io.github.rimonmostafiz.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rimon Mostafiz
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    OPEN("open"),
    IN_PROGRESS("in progress"),
    CLOSED("closed");

    private final String status;
}
