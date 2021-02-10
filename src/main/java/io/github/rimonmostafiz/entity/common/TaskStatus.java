package io.github.rimonmostafiz.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Rimon Mostafiz
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    CLOSED;

    public static Optional<TaskStatus> getStatus(String status) {
        return Arrays.stream(TaskStatus.values())
                .filter(v -> v.name().equals(status))
                .findFirst();
    }
}
