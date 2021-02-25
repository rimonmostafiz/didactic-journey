package io.github.rimonmostafiz.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModel {
    private Long id;

    private String description;

    private TaskStatus status;

    private ProjectModel project;

    private UserModel assignedUser;

    private LocalDate dueDate;
}
