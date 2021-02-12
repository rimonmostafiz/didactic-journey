package io.github.rimonmostafiz.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rimon Mostafiz
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Serializable {
    private String username;
    private String password;
}