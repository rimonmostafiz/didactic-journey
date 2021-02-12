package io.github.rimonmostafiz.model.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rimon Mostafiz
 */
@Getter
@AllArgsConstructor
public enum ActivityAction {
    INSERT(0),
    UPDATE(1),
    DELETE(2);

    private final int action;
}
