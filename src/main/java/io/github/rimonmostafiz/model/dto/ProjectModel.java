package io.github.rimonmostafiz.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectModel {
    private Long id;

    private String name;

    private String description;

    private Status status;

    private UserModel assignedUser;
}
