package io.github.rimonmostafiz.model;

import io.github.rimonmostafiz.entity.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Status status;
}