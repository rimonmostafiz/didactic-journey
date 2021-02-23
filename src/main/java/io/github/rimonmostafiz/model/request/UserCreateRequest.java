package io.github.rimonmostafiz.model.request;

import io.github.rimonmostafiz.model.entity.common.Status;
import lombok.Data;

/**
 * @author Rimon Mostafiz
 */
@Data
public class UserCreateRequest {
    private String username;

    private String password;

    private String email;

    private String firstName;

    private String LastName;

    private Status status;
}
