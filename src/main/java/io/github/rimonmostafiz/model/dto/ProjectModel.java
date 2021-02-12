package io.github.rimonmostafiz.model.dto;

import io.github.rimonmostafiz.model.entity.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectModel {
    private Long id;

    private String name;

    private String description;

    private Status status;

    private UserModel assignedUser;

}
