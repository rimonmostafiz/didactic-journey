package io.github.rimonmostafiz.model.entity.db;

import io.github.rimonmostafiz.model.entity.common.EntityCommon;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static io.github.rimonmostafiz.utils.ValidationConstants.ALPHANUMERIC_UNDERSCORE_DOT;
import static io.github.rimonmostafiz.utils.ValidationConstants.ROLE_MAX_SIZE;

/**
 * @author Rimon Mostafiz
 */
@Entity
@NoArgsConstructor
@Table(name = "ROLE")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Role extends EntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotBlank(message = "{error.role.name.blank}")
    @Size(max = ROLE_MAX_SIZE, message = "{error.role.name.max.size}")
    @Pattern(regexp = ALPHANUMERIC_UNDERSCORE_DOT, message = "{error.role.name.invalid}")
    private String name;
}
