package io.github.rimonmostafiz.entity.db;

import io.github.rimonmostafiz.entity.common.EntityCommon;
import io.github.rimonmostafiz.entity.common.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static io.github.rimonmostafiz.utils.ValidationConstants.*;

/**
 * @author Rimon Mostafiz
 */
@Entity
@NoArgsConstructor
@Table(name = "USER")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class User extends EntityCommon {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", unique = true)
    @NotBlank(message = "{error.user.username.blank}")
    @Size(max = MAX_USERNAME_SIZE, message = "{error.user.username.max.size}")
    @Pattern(regexp = ALPHANUMERIC_UNDERSCORE_DOT, message = "{error.user.username.invalid}")
    private String username;

    @Column(name = "PASSWORD")
    @NotBlank(message = "{error.user.password.blank}")
    @Size(min = MIN_PASSWORD_SIZE, max = MAX_PASSWORD_SIZE, message = "{error.user.password.size}")
    private String password;

    @Column(name = "EMAIL", unique = true)
    @NotBlank(message = "{error.user.email.blank}")
    @Email(message = "{error.user.email.invalid}")
    @Size(max = MAX_EMAIL_SIZE, message = "{error.email.max.size}")
    private String email;

    @Column(name = "FIRST_NAME")
    @Size(max = MAX_FIRST_NAME_SIZE, message = "{error.user.firstName.max.size")
    private String firstName;

    @Column(name = "LAST_NAME")
    @Size(max = MAX_LAST_NAME_SIZE, message = "{error.user.lastName.max.size")
    private String lastName;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "{error.user.status.blank}")
    private Status status;
}
