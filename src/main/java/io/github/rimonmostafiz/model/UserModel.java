package io.github.rimonmostafiz.model;

import io.github.rimonmostafiz.entity.common.Status;
import lombok.Data;

/**
 * @author Rimon Mostafiz
 */
@Data
public class UserModel {
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Status status;
}